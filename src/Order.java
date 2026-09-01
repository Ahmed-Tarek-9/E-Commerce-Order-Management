import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private final int orderId;
    private final String customerName;
    private final Map<Integer, CartItem> cartItems;
    private OrderStatus status;
    private static int nextId = 1;

    public Order(String customerName) {
        this.customerName = customerName;
        orderId = nextId++;
        status = OrderStatus.PENDING;
        cartItems = new LinkedHashMap<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Map<Integer, CartItem> getCartItems() {
        return Map.copyOf(cartItems);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void addItem(CartItem cartItem) {
        cartItems.merge(cartItem.getProduct().getId(), cartItem, (existing, incoming) -> {
            existing.addQuantity(incoming.getQuantity());
            return existing;
        });

        cartItem.getProduct().removeStockQuantity(cartItem.getQuantity());
    }

    public void removeItem(CartItem cartItem) {
        cartItems.remove(cartItem.getProduct().getId());
        cartItem.getProduct().addStockQuantity(cartItem.getQuantity());
    }

    public double calculateTotal() {
        return cartItems.values().stream().mapToDouble(CartItem::calculateSubtotal).sum();
    }

    public void displayOrder() {
        System.out.println("----------------------------------------");
        System.out.println("Order ID: #" + orderId);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Status: " + status);
        System.out.println("Order Items:");
        cartItems.forEach((productId, cartItem) ->
            System.out.println("\tID: " + productId + ", " + cartItem.getProduct().getName() + " x " + cartItem.getQuantity()
                    + " = " + cartItem.getProduct().getPrice() * cartItem.getQuantity()));
        System.out.println("Total = " + calculateTotal());
        System.out.println("----------------------------------------");
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}
