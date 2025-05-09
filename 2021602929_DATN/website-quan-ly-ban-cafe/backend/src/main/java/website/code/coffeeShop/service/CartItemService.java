package website.code.coffeeShop.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.code.coffeeShop.model.Cart;
import website.code.coffeeShop.model.Product;
import website.code.coffeeShop.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    public void setCartRepository(CartRepository cartRepository) {this.cartRepository = cartRepository;}

    public void setProductService(ProductService productService) {this.productService = productService;}

    @Getter
    public static class CartItemWithProduct{
        private Cart cartItem;
        private Product product;

        public CartItemWithProduct(Cart cartItem, Product product) {
            this.cartItem = cartItem;
            this.product = product;
        }
    }

    public List<CartItemWithProduct> getCartItemsByCustomerId(int customerId){
        Iterable<Cart> cartItems = cartRepository.findByUid(customerId);
        List<CartItemWithProduct> cartItemWithProducts = new ArrayList<>();

        for(Cart cartItem : cartItems){
            Product product = productService.getProductByPid(cartItem.getPid());
            cartItemWithProducts.add(new CartItemWithProduct(cartItem, product));
        }
        return cartItemWithProducts;
    }

    public void deleteCartItem(int id){cartRepository.deleteById(id);}

    public Optional<Cart> getCartById(int id){return cartRepository.findById(id);}

    public void addCartItem(Cart cartItem){
        Optional<Cart> existingCartItem = cartRepository.findByPidAndUid(cartItem.getPid(), cartItem.getUid());
        if(existingCartItem.isPresent()){
            Cart existingItem = existingCartItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
            float newTotalCost = existingItem.getQuantity() * productService.getProductByPid(existingItem.getPid()).getPrice();
            existingItem.setTotalCost(newTotalCost);
            cartRepository.save(existingItem);
        }else {
            cartRepository.save(cartItem);
        }
    }

    public void clearCart(){cartRepository.deleteAll();}

    public float calculateTotalPrice(int customerId){
        List<CartItemWithProduct> cartItems = getCartItemsByCustomerId(customerId);
        float totalPrice = 0.0F;
        for(CartItemWithProduct cartItemWithProduct : cartItems){
            totalPrice += cartItemWithProduct.getProduct().getPrice() * cartItemWithProduct.getCartItem().getQuantity();
        }
        return totalPrice;
    }

    public void updateCartItemQuantity(int cartItemId, int newQuantity){
        Optional<Cart> optionalCart = cartRepository.findById(cartItemId);
        if(optionalCart.isPresent()){
            Cart cartItem = optionalCart.get();
            cartItem.setQuantity(newQuantity);
            float newTotalCost = newQuantity * productService.getProductByPid(cartItem.getPid()).getPrice();
            cartItem.setTotalCost(newTotalCost);
            cartRepository.save(cartItem);
        }
    }
}
