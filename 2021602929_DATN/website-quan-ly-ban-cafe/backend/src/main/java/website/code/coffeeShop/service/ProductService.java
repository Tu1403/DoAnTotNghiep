package website.code.coffeeShop.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import website.code.coffeeShop.model.Category;
import website.code.coffeeShop.model.Product;
import website.code.coffeeShop.repository.CategoryRepository;
import website.code.coffeeShop.repository.ProductRepository;

import javax.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void deleteProductById(int id) {productRepository.deleteById(id);}

    public void saveProduct(Product product) {productRepository.save(product);}

    public Optional<Product> getProductById(int id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(this::setCategoryName);
        return product;
    }

    private void setCategoryName(Product product) {
        String CategoryName = categoryRepository.findById(product.getCategoryId())
                .map(Category::getCategoryName)
                .orElse("Unknown");
        product.setCategoryName(CategoryName);
    }

    public Iterable<Product> getAllProducts() {
        Iterable<Product> products = productRepository.findAll();
        products.forEach(this::setCategoryName);
        return products;
    }

    public Iterable<Product> getProductsByCategory(int cid) {
        Iterable<Product> products = productRepository.findByCategoryId(cid);
        products.forEach(this::setCategoryName);
        return products;
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> getLastestProducts(){
        List<Product> products = productRepository.getTop3Products();
        products.forEach(this::setCategoryName);
        return products;
    }

    public Page<Product> getProducts(int page, int size){
        Page<Product> productPage = productRepository.findAll(PageRequest.of(page, size));
        productPage.forEach(this::setCategoryName);
        return productPage;
    }

    public Product getProductByPid(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        if(product != null){
            setCategoryName(product);
        }
        return product;
    }

    public Page<Product> getProductByCategories(int page, int size, int cid){
        Page<Product> productPage = productRepository.findByCategoryId(cid, PageRequest.of(page, size));
        productPage.forEach(this::setCategoryName);
        return productPage;
    }

    public Page<Product> getProductManagement(int page, int size){
        return productRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Product> searchProducts(String query, Integer categoryId, Pageable pageable){
        Page<Product> productPage;
        if(categoryId != null){
            productPage = productRepository.findByPnameContainingIgnoreCaseAndCategoryId(query, categoryId, pageable);
        }else {
            productPage = productRepository.findByPnameContainingIgnoreCase(query, pageable);
        }
        productPage.forEach(this::setCategoryName);
        return productPage;
    }

    @Autowired
    public ProductService(ProductRepository productRepository){this.productRepository = productRepository;}

    public void deleteProductsByCategoryId(int cid) {
        Iterable<Product> products = productRepository.findByCategoryId(cid);
        productRepository.deleteAll(products);
    }

    public Page<Product> getProducts(Pageable pageable){return productRepository.findAll(pageable);}

    public Page<Product> getProductsByCategory(int categoryId, Pageable pageable){
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Product> searchProducts2(String pname, Pageable pageable){
        return productRepository.findByPnameContaining(pname, pageable);
    }

    public void updateProductWithImage(MultipartFile file, Product product) throws IOException {
        if(file != null && !file.isEmpty()){
            String fileName = StringUtils. cleanPath(file.getOriginalFilename());
            if(fileName.contains("..")){
                throw new ValidationException("Not a valid file !");
            }
            product.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        }
        validateProductInput(product.getPname(), product.getUnit(), product.getQuantity(), product.getPrice(), product.getCategoryId());
        productRepository.save(product);
    }

    private void validateProductInput(String pname, String unit, int quantity, float price, int categoryId) {
        if(pname == null || pname.trim().isEmpty()){
            throw new ValidationException("Tên sản phẩm không được để trống !");
        }

        if(unit == null || unit.trim().isEmpty()){
            throw new ValidationException("Đơn vị không được để trống !");
        }

        if(quantity < 0) {
            throw new ValidationException("Số lượng phải là số nguyên không âm !");
        }

        if(price < 0){
            throw new ValidationException("Giá phải là số không âm !");
        }

        if(categoryId <= 0) {
            throw new ValidationException("Danh mục không được để trống !");
        }
    }

    public void saveProductToDB(MultipartFile file, String pname, String description, String unit, int quantity, float price, int categoryId) {
        validateProductInput(pname, unit, quantity, price, categoryId);
        Product product = new Product();
        String fileName = StringUtils. cleanPath(file.getOriginalFilename());
        String contentType = file.getContentType();
        if(fileName.contains("..")){
            System.out.println("invalid file");
        }

        if(contentType == null || !contentType.startsWith("image/")) {
            throw new ValidationException("File không phải là hình ảnh !");
        }

        try{
            product.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        product.setPname(pname);
        product.setDescription(description);
        product.setUnit(unit);
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setCategoryId(categoryId);

        productRepository.save(product);
    }

    public List<Product> getAllProduct() {return productRepository.findAll();}

    public boolean isProductNameExists(String pname) {return productRepository.existsByPname(pname);}

    public boolean isProductNameExistsForOther(int pid, String pname) {return productRepository.existsByPnameAndPidNot(pname, pid);}

    public void saveProductsFromExcelFile(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();
            List<Product> products = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if(rowNumber == 0){
                    rowNumber++;
                    continue;
                }

                Product product = new Product();
                product.setPname(currentRow.getCell(0).getStringCellValue());
                product.setDescription(currentRow.getCell(1).getStringCellValue());
                product.setUnit(currentRow.getCell(2).getStringCellValue());

                // Kiểm tra quantity là số dương
                double quantityValue = currentRow.getCell(3).getNumericCellValue();
                if(quantityValue <= 0 || (int) quantityValue != quantityValue){
                    throw new ValidationException("Quantity ở dòng " + (rowNumber + 1) + " phải là số nguyên dương.");
                }
                product.setQuantity((int) quantityValue);

                // Kiểm tra price là số dương
                double priceValue = currentRow.getCell(4).getNumericCellValue();
                if (priceValue <= 0) {
                    throw new ValidationException("Price ở dòng " + (rowNumber + 1) + " phải là số dương.");
                }
                product.setPrice((float) priceValue);

                // Resolve Category Name to ID
                String categoryName = currentRow.getCell(5).getStringCellValue();
                Category category = categoryRepository.findCategoriesByCategoryName(categoryName);
                if (category != null) {
                    product.setCategoryId(category.getCid());
                } else {
                    throw new RuntimeException("Không tìm thấy danh mục: " + categoryName);
                }

                products.add(product);
            }
            workbook.close();
            productRepository.saveAll(products);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Chuyển đổi sang file Excel thất bại: " + e.getMessage());
        }
    }
}
