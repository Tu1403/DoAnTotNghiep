package website.code.coffeeShop.service;

import org.springframework.stereotype.Service;
import website.code.coffeeShop.model.CartItem;
import website.code.coffeeShop.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(Product product, int quantity) {
        Optional<CartItem> existingItem = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getPid() == product.getPid())
                .findFirst();
        if(existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }else {
            cartItems.add(new CartItem(product, quantity));
        }
    }
    public List<CartItem> getCartItems() {return cartItems;}

    public void deleteProductFromCart(int productId) {
        cartItems.removeIf(cartItem -> cartItem.getProduct().getPid() == productId);
    }

    public void clearCart(){cartItems.clear();}

    public float getTotalPrice(){
        return (float) cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public void setQuantity(Product product) {product.setQuantity(1);}
}
