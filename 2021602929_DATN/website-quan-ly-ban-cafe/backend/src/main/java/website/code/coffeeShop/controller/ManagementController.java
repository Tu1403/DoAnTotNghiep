package website.code.coffeeShop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import website.code.coffeeShop.model.*;
import website.code.coffeeShop.service.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private BillService billService;

    @Autowired
    private BillDetailService billDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private TableService tableService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/management")
    public String management(@RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size,
                             Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Product> productPage = productService.getProducts(currentPage - 1, pageSize);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("tables", tableService.getAllTables());
        return "management";
    }


    @GetMapping("/management/products/{cid}")
    public String productByCategory(@PathVariable int cid, Model model,
                                    @RequestParam("page") Optional<Integer> page,
                                    @RequestParam("size") Optional<Integer> size,
                                    Principal principal) {

        if (principal != null) {
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        int currentPage = page.orElse(1);

        List<Integer> pageNumbers = IntStream.rangeClosed(1, 1)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);

        Iterable<Product> listP = productService.getProductsByCategory(cid);
        Iterable<Category> listC = categoryService.getAllCategories();
        model.addAttribute("products", listP);
        model.addAttribute("categories", listC);
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("tables", tableService.getAllTables());
        return "management";
    }

    @GetMapping("/management/products/{pid}/addtocart")
    public String addToCart(@PathVariable int pid, @RequestParam(value = "quantity", defaultValue = "1") int quantity, Model model) {
        Optional<Product> productOptional = productService.getProductById(pid);
        if (productOptional.isPresent() && productOptional.get().getQuantity() >= quantity) {
            cartService.addToCart(productOptional.get(), quantity);
        } else {
            model.addAttribute("error", "Không tìm thấy");
            model.addAttribute("message", "Số lượng sản phẩm không đủ rồi!");
        }
        return "redirect:/management";
    }

    @GetMapping("/management/delete/{pid}")
    public String delete(@PathVariable int pid, RedirectAttributes redirectAttributes) {
//        Optional<Product> productOptional = productService.getProductById(pid);
        cartService.deleteProductFromCart(pid);
        return "redirect:/management";
    }

    @PostMapping("/createBill")
    public String createBill(@RequestParam("numberOfGuest") int numberOfGuest,
                             @RequestParam("tableId") int tableId,
                             Principal principal,
                             Model model,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size) {

        if (principal != null) {
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            model.addAttribute("user", user);

            int currentPage = page.orElse(1);
            int pageSize = size.orElse(6);

            Page<Product> productPage = productService.getProducts(currentPage - 1, pageSize);

            int totalPages = productPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }

            logger.info("createBill called with numberOfGuest: {}, tableId: {}", numberOfGuest, tableId);

            // Get cart items and calculate total cost
            List<CartItem> cartItems = cartService.getCartItems();

            float totalCost = 0;
            for (int i = 0; i < cartItems.size(); i++) {
                CartItem item = cartItems.get(i);
                totalCost += item.getProduct().getPrice() * item.getQuantity();

            }

            // Create a new Bill
            Bill bill = new Bill("", "", new Date(), numberOfGuest, totalCost, tableId, user.getUid(), 0, 1);
            billService.save(bill);
            tableService.updateTableStatus(tableId, 1);
            logger.info("Bill saved with ID: {}", bill.getBillId());

            // Create BillDetail entries for each cart item
            for (int i = 0; i < cartItems.size(); i++) {
                CartItem item = cartItems.get(i);
                BillDetail billDetail = new BillDetail(bill.getBillId(), item.getProduct().getPid(), item.getQuantity(), item.getProduct().getPrice());
                billDetailService.save(billDetail);
                logger.info("BillDetail saved for billId: {}, productId: {}, quantity: {}, price: {}", bill.getBillId(), item.getProduct().getPid(), item.getQuantity(), item.getProduct().getPrice());

                Product product = item.getProduct();
                int newQuantity = product.getQuantity() - item.getQuantity();
                if (newQuantity < 0) {
                    throw new IllegalArgumentException("Số lượng sản phẩm không đủ trong kho!");
                }
                product.setQuantity(newQuantity);
                productService.updateProduct(product);
            }

            // Clear the cart
            cartService.clearCart();
            logger.info("Cart cleared");

            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("products", productPage.getContent());
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("successMessage", "Tạo Bill thành công!");
        }

        return "management";
    }


    @GetMapping("/management/allbill")
    public String allBill(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size,
                          @RequestParam(value = "createdTime", required = false) String createdTime,
                          Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> billsPage;
        boolean noBills = false;
        if (createdTime != null && !createdTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(createdTime, formatter);
            Date dateValue = java.sql.Date.valueOf(date);
            billsPage = billService.searchByCreatedTime(dateValue, pageable);
            noBills = billsPage.isEmpty();
        } else {
            billsPage = billService.getAllBills(pageable);
        }
//        Iterable<Bill> listBill = billService.getAllBills();
        model.addAttribute("bills", billsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", billsPage.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("createdTime", createdTime);
        model.addAttribute("noBills", noBills);
        return "allbill-cashier";
    }

    @GetMapping("/management/billdetail/{billId}")
    public String viewBillDetail(@PathVariable("billId") int billId, Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        Bill bill = billService.findById(billId);
        List<BillDetail> billDetails = billDetailService.findByBillId(billId);
        float totalCost = billService.calculateTotalCost(billId);

        billDetails.forEach(billDetail -> {
            Product product = billDetail.getProduct();
            if (product != null && product.getImage() != null) {
                // Nếu cần, kiểm tra và xử lý thêm trước khi đẩy vào giao diện
                product.setImage("data:image/jpeg;base64," + product.getImage());
            }
        });

        model.addAttribute("bill", bill);
        model.addAttribute("billDetails", billDetails);
        model.addAttribute("totalCost", totalCost);
        return "billDetail :: billDetailModalContent";
    }

    @PostMapping("/management/updateBillStatus")
    public String updateBillStatus(@RequestParam("billId") int billId, @RequestParam("status") int status, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal != null) {
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            redirectAttributes.addAttribute("user", user);
        }

        // Validate the status value (1 for paid, 0 for not paid)
        if (status != 0 && status != 1) {
            redirectAttributes.addAttribute("error", "Giá trị trạng thái không hợp lệ");
            return "redirect:/management/allbill";
        }

        // Update the bill status
        Bill bill = billService.findById(billId);
        if (bill != null) {
            bill.setStatus(status);
            billService.save(bill);
            logger.info("Updated status for billId: {} to status: {}", billId, status);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái của Bill thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy Bill");
        }

        return "redirect:/management/allbill";
    }

    @GetMapping("/management/allbill/export/bill")
    @ResponseBody
    public ResponseEntity<InputStreamResource> exportBillToExcel(@RequestParam("billId") int billId) throws IOException {
        Bill bill = billService.findById(billId);
        int uid = bill.getUserId();
        List<BillDetail> billDetails = billDetailService.findByBillId(billId);

        ByteArrayInputStream in = excelExportService.exportBillToExcel(bill,uid, billDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=bill_" + billId + ".xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(in));
    }

}
