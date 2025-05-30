package website.code.coffeeShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import website.code.coffeeShop.model.Category;
import website.code.coffeeShop.model.Product;
import website.code.coffeeShop.model.Users;
import website.code.coffeeShop.service.CartItemService;
import website.code.coffeeShop.service.CategoryService;
import website.code.coffeeShop.service.ProductService;
import website.code.coffeeShop.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/products")
    public String products(Model model,
                           @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "15") int pageSize,
                           @RequestParam(required = false) Integer categoryId,
                           @RequestParam(required = false) String keyword,
                           Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        Page<Product> productPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            productPage = productService.searchProducts2(keyword, PageRequest.of(pageNo - 1, pageSize));
        } else if (categoryId != null) {
            productPage = productService.getProductsByCategory(categoryId, PageRequest.of(pageNo - 1, pageSize));
        } else {
            productPage = productService.getProducts(PageRequest.of(pageNo - 1, pageSize));
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPage", productPage.getTotalPages());  // Đảm bảo rằng bạn đã thêm thuộc tính này
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        return "homepage";
    }

    @GetMapping("/homepage")
    public String homepage(Model model, Principal principal) {
        System.err.println("123123");
        if (principal != null) {
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            int userId = user.getUid();
            List<CartItemService.CartItemWithProduct> cartItems = cartItemService.getCartItemsByCustomerId(userId);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("user", user);
        }
        List<Product> listP = productService.getLastestProducts();
        Iterable<Category> listC = categoryService.getAllCategories();
        model.addAttribute("categories", listC);
        model.addAttribute("products", listP);
        model.addAttribute("currentPage", "home");
        return "home";
    }

    @GetMapping("/products/update/{pid}")
    public String updateProduct(@PathVariable int pid, Model model) {
        Optional<Product> product = productService.getProductById(pid);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "updateproductdemo";
        } else {
            return "redirect:/products";
        }
    }

    @PostMapping("/products/update")
    public String saveUpdatedProduct(@RequestParam("pid") int pid,
                                     @RequestParam("pname") String pname,
                                     @RequestParam("description") String description,
                                     @RequestParam("unit") String unit,
                                     @RequestParam("quantity") int quantity,
                                     @RequestParam("price") float price,
                                     @RequestParam("categoryId") int categoryId,
                                     @RequestParam("file") MultipartFile file,
                                     RedirectAttributes redirectAttributes
                                     ) {

        // Kiểm tra nếu tên sản phẩm đã tồn tại cho sản phẩm khác
        if (productService.isProductNameExistsForOther(pid, pname)) {
            return "redirect:/products";
        }

        Optional<Product> existingProduct = productService.getProductById(pid);
        if (existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            updatedProduct.setPname(pname);
            updatedProduct.setDescription(description);
            updatedProduct.setUnit(unit);
            updatedProduct.setQuantity(quantity);
            updatedProduct.setPrice(price);
            updatedProduct.setCategoryId(categoryId);

            if (!file.isEmpty()) {
                try {
                    updatedProduct.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("message", "Failed to upload image");
                    return "redirect:/products";
                }
            }
            productService.updateProduct(updatedProduct);
//  productService.saveProductToDB(file, pname, description, unit, quantity, price, categoryId);
            redirectAttributes.addFlashAttribute("message", "Cập nhật sản phẩm thành công !");
        } else {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy sản phẩm !");
        }
        return "redirect:/products";
    }


    @PostMapping("/products/add")
    public String saveNewProduct(@ModelAttribute("product") Product product,
                                 BindingResult bindingResult,
                                 @RequestParam("file") MultipartFile file,
                                 Model model,
                                 HttpSession session) {

        // Kiểm tra nếu tên sản phẩm đã tồn tại
        if (productService.isProductNameExists(product.getPname())) {
            bindingResult.rejectValue("pname", "error.product", "Tên sản phẩm đã tồn tại");
            model.addAttribute("errorMessage", "Tên sản phẩm đã tồn tại");
            model.addAttribute("categories", categoryService.getAllCategories());
            return "addproduct";
        }

        String imagePath = null;
        try {
            if (!file.isEmpty()) {
                // imagePath = saveTemporaryFile(file);
                // session.setAttribute("imagePath", imagePath);
            }

            productService.saveProductToDB(file, product.getPname(), product.getDescription(), product.getUnit(), product.getQuantity(), product.getPrice(), product.getCategoryId());
            session.removeAttribute("imagePath");
            model.addAttribute("message", "Thêm sản phẩm thành công !");
            return "redirect:/products";
        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());

            return "addproduct";
        }
    }

    private String saveTemporaryFile(MultipartFile file) throws IOException {
        // Lưu file tạm thời và trả về đường dẫn
        String tempDir = System.getProperty("java.io.tmpdir");
        String fileName = file.getOriginalFilename();
        File tempFile = new File(tempDir, fileName);
        file.transferTo(tempFile);
        return tempFile.getAbsolutePath();
    }

    @GetMapping("/menu")
    public String showMenu(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "5") int pageSize,
                           @RequestParam(required = false) Integer categoryId,
                           @RequestParam(required = false) String search,
                           Principal principal) {

        // Handle principal and cart items if logged in
        if (principal != null) {
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            int userId = user.getUid();
            List<CartItemService.CartItemWithProduct> cartItems = cartItemService.getCartItemsByCustomerId(userId);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("user", user);
        }

        Page<Product> productPage;
        boolean noProductsFound = false;
        if (search != null && !search.trim().isEmpty()) {
            productPage = productService.searchProducts(search, categoryId, PageRequest.of(page, pageSize));
            noProductsFound = productPage.isEmpty();

        } else if (categoryId != null) {
            productPage = productService.getProductByCategories(page, pageSize, categoryId);
            noProductsFound = productPage.isEmpty();

        } else {
            productPage = productService.getProducts(page, pageSize);
        }

        // Add attributes to the model
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("searchQuery", search);
        model.addAttribute("noProductsFound", noProductsFound);
        model.addAttribute("currentPage1", "menu");
        model.addAttribute("pageSize", pageSize);  // Add the page size to the model

        return "menu";
    }

    @GetMapping("/product/{pid}")
    public String viewProductDetails(@PathVariable("pid") Integer pid, Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Users user = userService.getUserByUsername(username);
            int userId = user.getUid();
            List<CartItemService.CartItemWithProduct> cartItems = cartItemService.getCartItemsByCustomerId(userId);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("user", user);
        }
        Product product = productService.getProductByPid(pid);
        if (product == null) {
            return "error/404";
        }
        model.addAttribute("product", product);
        model.addAttribute("categoryName", product.getCategoryName());
        model.addAttribute("currentPage", "menu");
        return "productdetail";
    }

    @GetMapping("/products/delete/{pid}")
    public String deleteProduct(@PathVariable int pid, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProductById(pid);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được xóa thành công.");
        } catch (Exception e) {
            // Giả sử lỗi xảy ra là do sản phẩm còn được sử dụng trong hóa đơn
            redirectAttributes.addFlashAttribute("messageErr", "Sản phẩm còn được sử dụng trong bill.");
        }
        return "redirect:/products";
    }


    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "addproduct";
    }

    @PostMapping("/products/import")
    public String importProductsFromFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("messageErr", "Hãy chọn một file excel để đăng tải !");
            return "redirect:/products";
        }

        try {
            productService.saveProductsFromExcelFile(file);
            redirectAttributes.addFlashAttribute("message", "Đăng tải sản phẩm  thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageErr", "Đăng tải sản phẩm thất bại: " + e.getMessage());
        }

        return "redirect:/products";
    }

}
