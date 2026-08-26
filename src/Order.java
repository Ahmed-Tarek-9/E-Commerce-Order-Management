import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private int orderId;
    private String customerName;
    private Map<Integer, CartItem> cartItems;
    private double total;
    private OrderStatus status;
    private static int nextId = 1;

    public Order(String customerName) {
        this.customerName = customerName;
        orderId = nextId++;
        status = OrderStatus.PENDING;
        cartItems = new LinkedHashMap<Integer, CartItem>();
        total = 0.0;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getTotal() {
        return total;
    }

    public Map<Integer, CartItem> getCartItems() {
        return cartItems;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void addItem(CartItem cartItem) {
        if (cartItems.containsKey(cartItem.getProduct().getId())) {
            cartItems.get(cartItem.getProduct().getId()).addQuantity(cartItem.getQuantity());
        } else {
            cartItems.put(cartItem.getProduct().getId(), cartItem);
        }
        calculateTotal();
        cartItem.getProduct().removeStockQuantity(cartItem.getQuantity());
    }

    public void removeItem(CartItem cartItem) {
        cartItems.remove(cartItem.getProduct().getId());
        calculateTotal();
        cartItem.getProduct().addStockQuantity(cartItem.getQuantity());
    }

    public double calculateTotal() {
        total = 0.0;
        for (CartItem cartItem : cartItems.values()) {
            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return total;
    }

    public void displayOrder() {
        System.out.println("Order ID: #" + orderId);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Status: " + status);
        System.out.println("Order Items:");
        cartItems.forEach((productId, cartItem) ->
            System.out.println("\tID: " + productId + ", " + cartItem.getProduct().getName() + " x " + cartItem.getQuantity()
                    + " = " + cartItem.getProduct().getPrice() * cartItem.getQuantity()));
        System.out.println("Total = " + total);
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}
