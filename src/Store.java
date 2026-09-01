import java.util.*;

public class Store {
    private final List<Product> products;
    private final Map<Integer, Product> productMap;
    private final Map<Integer, Order> orders;
    private final Set<String> categories;
    private final Queue<Order> shippingQueue;
    private final List<Order> deliveredOrders;
    private final List<Review> reviews;

    public Store() {
        products = new ArrayList<>();
        productMap = new HashMap<>();
        orders = new HashMap<>();
        categories = new HashSet<>();
        shippingQueue = new ArrayDeque<>();
        deliveredOrders = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    private int readPositiveInt(Scanner scanner, String message) {
        while (true) {
            System.out.println(message);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    return value;
                }
                System.out.println("Invalid input. Please enter a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private int readIntBetweenRange(Scanner scanner, String message, int start, int end) {
        while (true) {
            System.out.println(message);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= start && value <= end) {
                    return value;
                }
                System.out.println("Invalid input. Please enter a number in the range [" + start + "->" + end + "]");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private double readPositiveDouble(Scanner scanner, String message) {
        while (true) {
            System.out.println(message);
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value > 0) {
                    return value;
                }
                System.out.println("Invalid input. Please enter a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private String readNonEmptyString(Scanner scanner, String message) {
        while(true) {
            System.out.println(message);
            String value = scanner.nextLine();
            if(!value.trim().isEmpty()) {
                return value;
            }
            System.out.println("The string cannot be empty.");
        }
    }

    public void addProduct(Scanner scanner) {
        String name = readNonEmptyString(scanner, "Enter the product's name: ");

        double price = readPositiveDouble(scanner, "Enter the product's price");

        String category = readNonEmptyString(scanner, "Enter the product's category: ");
        String resultCategory = category.substring(0, 1).toUpperCase() + category.substring(1);

        int quantity = readPositiveInt(scanner, "Enter the product's stock quantity: ");

        Product product = new Product(name, price, resultCategory, quantity);
        products.add(product);
        productMap.put(product.getId(), product);
        categories.add(resultCategory);
        System.out.println("Product added successfully.");
    }

    public void removeProduct(Scanner scanner) {
        int id = readPositiveInt(scanner, "Enter the product ID: ");

        Product product = productMap.get(id);
        if (product == null) {
            System.out.println("Product not found.");
        } else {
            products.remove(product);
            productMap.remove(id);
            System.out.println("Product removed successfully.");
            boolean categoryStillExists = products.stream().anyMatch(p -> p.getCategory().equals(product.getCategory()));
            if (!categoryStillExists) {
                categories.remove(product.getCategory());
            }
        }
    }

    public void displayProducts() {
        products.forEach(product -> {
            System.out.println("----------------------------------------");
            System.out.println(product);
            System.out.println("----------------------------------------");
        });
    }

    public void searchProduct(Scanner scanner) {
        int id = readPositiveInt(scanner, "Enter the product ID: ");

        if (!productMap.containsKey(id)) {
            System.out.println("Product not found.");
        } else {
            System.out.println("Product found:");
            System.out.println(productMap.get(id));
        }
    }

    public void displayCategories() {
        System.out.println("Categories:");
        categories.forEach(category -> System.out.println("\t- " + category));
    }

    public void displayProductsByPrice() {
        ArrayList<Product> sortedProducts = new ArrayList<>(products);
        sortedProducts.sort(null);
        sortedProducts.forEach(product -> {
            System.out.println("----------------------------------------");
            System.out.println(product);
            System.out.println("----------------------------------------");
        });
    }

    public void createOrder(Scanner scanner) {
        String customerName = readNonEmptyString(scanner, "Enter the customer's name: ");

        Order order = new Order(customerName);
        orders.put(order.getOrderId(), order);
        System.out.println("Order created successfully. Order ID: #" + order.getOrderId());
    }

    public void addItemToOrder(Scanner scanner) {
        int orderId = readPositiveInt(scanner, "Enter the order ID: ");

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Order is already " + order.getStatus() + ".");
        } else {
            int productId = readPositiveInt(scanner, "Enter the product ID: ");

            Product product = productMap.get(productId);
            if (product == null) {
                System.out.println("Product not found.");
            } else {
                int quantity = readPositiveInt(scanner, "Enter the quantity");

                if (product.getStockQuantity() < quantity) {
                    System.out.println("Not enough stock.");
                } else {
                    CartItem cartItem = new CartItem(product, quantity);
                    order.addItem(cartItem);
                    System.out.println("Item added to order successfully.");
                }
            }
        }
    }

    public void removeItemFromOrder(Scanner scanner) {
        int orderId = readPositiveInt(scanner, "Enter the order ID: ");

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else if (order.getStatus() != OrderStatus.PENDING) {
                System.out.println("Order is already " + order.getStatus() + ".");
        } else {
            order.displayOrder();
            int itemId = readPositiveInt(scanner, "Enter the item ID: ");

            if(!order.getCartItems().containsKey(itemId)) {
                System.out.println("Item not found.");
            } else {
                CartItem cartItem = order.getCartItems().get(itemId);
                order.removeItem(cartItem);
                System.out.println("Item removed successfully.");
            }
        }
    }

    public void displayOrder(Scanner scanner) {
        int orderId = readPositiveInt(scanner, "Enter the order ID: ");

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else {
            order.displayOrder();
        }
    }

    public void addOrderToShippingList(Scanner scanner) {
        int orderId = readPositiveInt(scanner, "Enter the order ID: ");

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else if (order.getStatus() != OrderStatus.PENDING){
            System.out.println("Order is already " + order.getStatus() + ".");
        } else if (order.getCartItems().isEmpty()) {
            System.out.println("Order is empty.");
        } else {
            order.updateStatus(OrderStatus.SHIPPED);
            shippingQueue.add(order);
            System.out.println("Order added to shipping list.");
        }
    }

    public void shipNextOrder() {
        if (shippingQueue.isEmpty()) {
            System.out.println("No orders in shipping list.");
        } else {
            Order order = shippingQueue.poll();
            deliveredOrders.add(order);
            order.updateStatus(OrderStatus.DELIVERED);
            System.out.println("Order #" + order.getOrderId() + " delivered.");
        }
    }

    public void cancelOrder(Scanner scanner) {
        int orderId = readPositiveInt(scanner, "Enter the order ID: ");

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Order is already " + order.getStatus() + ".");
        } else {
            if (order.getStatus() == OrderStatus.SHIPPED) {
                shippingQueue.remove(order);
            }
            order.updateStatus(OrderStatus.CANCELLED);
            System.out.println("Order #" + order.getOrderId() + " cancelled.");
        }
    }

    public void searchOrderById(Scanner scanner) {
        int orderId = readPositiveInt(scanner, "Enter the order ID: ");

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else {
            order.displayOrder();
        }
    }

    public void addReview(Scanner scanner) {
        int productId = readPositiveInt(scanner, "Enter the product ID: ");

        if(!productMap.containsKey(productId)) {
            System.out.println("Product not found.");
        } else {
            String customerName = readNonEmptyString(scanner, "Enter the customer's name: ");

            int starRating = readIntBetweenRange(scanner, "Enter the star rating: ", 0, 5);

            System.out.println("Enter your comment on the product: ");
            String comment = scanner.nextLine();
            Review review = new Review (productId, customerName, starRating, comment);
            reviews.add(review);
        }
    }

    public void displayReviewsForProduct(Scanner scanner) {
        int productId = readPositiveInt(scanner, "Enter the product ID: ");
        List<Review> filtered = reviews.stream().filter(review -> review.productId() == productId).toList();
        if(filtered.isEmpty()) {
            System.out.println("No reviews found for product ID " + productId + ".");
        } else {
            filtered.forEach(review -> {
                System.out.println("----------------------------------------");
                System.out.println(review);
                System.out.println("----------------------------------------");
            });
        }
    }

    public void removeOutOfStockProducts() {
        Iterator<Product> it = products.iterator();
        while (it.hasNext()) {
            Product product = it.next();
            if (product.getStockQuantity() == 0) {
                productMap.remove(product.getId());
                it.remove();
            }
        }
    }

    public void addStockToProduct(Scanner scanner) {
        int productId = readPositiveInt(scanner, "Enter the product ID: ");

        if (!productMap.containsKey(productId)) {
            System.out.println("Product not found.");
        } else {
            int quantity = readPositiveInt(scanner, "Enter the quantity: ");

            productMap.get(productId).addStockQuantity(quantity);
            System.out.println("Quantity added successfully.");
            System.out.println(productMap.get(productId));
        }
    }

    public void displayOrdersOrderedByTotal() {
        List<Order> orders = new ArrayList<>(this.orders.values());
        orders.sort(Comparator.comparingDouble(Order::calculateTotal));
        orders.forEach(Order::displayOrder);
    }

    public void printMenu() {
        System.out.println("======== E-Commerce Store Menu =========");
        System.out.println("| 1. Add product to store              |");
        System.out.println("| 2. Remove product from store         |");
        System.out.println("| 3. Display all products              |");
        System.out.println("| 4. Search product by ID              |");
        System.out.println("| 5. Show all categories               |");
        System.out.println("| 6. Display products ordered by price |");
        System.out.println("| 7. Create order                      |");
        System.out.println("| 8. Add item to order                 |");
        System.out.println("| 9. Remove item from order            |");
        System.out.println("| 10. Display Order                    |");
        System.out.println("| 11. Add order to the shipping list   |");
        System.out.println("| 12. Ship next order                  |");
        System.out.println("| 13. Cancel order                     |");
        System.out.println("| 14. Search order by ID               |");
        System.out.println("| 15. Add review to a product          |");
        System.out.println("| 16. Show all reviews for a product   |");
        System.out.println("| 17. Remove out-of-stock products     |");
        System.out.println("| 18. Add stock to products            |");
        System.out.println("| 19. Display orders ordered by total  |");
        System.out.println("| 20. Exit                             |");
        System.out.println("========================================");
    }

    public void run() {
        int choice;
        Scanner scanner = new Scanner(System.in);
        do {
            printMenu();
            choice = readIntBetweenRange(scanner, "Enter your choice of service from 1 -> 20: ", 1, 20);

            switch (choice) {
                case 1 -> addProduct(scanner);
                case 2 -> removeProduct(scanner);
                case 3 -> displayProducts();
                case 4 -> searchProduct(scanner);
                case 5 -> displayCategories();
                case 6 -> displayProductsByPrice();
                case 7 -> createOrder(scanner);
                case 8 -> addItemToOrder(scanner);
                case 9 -> removeItemFromOrder(scanner);
                case 10 -> displayOrder(scanner);
                case 11 -> addOrderToShippingList(scanner);
                case 12 -> shipNextOrder();
                case 13 -> cancelOrder(scanner);
                case 14 -> searchOrderById(scanner);
                case 15 -> addReview(scanner);
                case 16 -> displayReviewsForProduct(scanner);
                case 17 -> removeOutOfStockProducts();
                case 18 -> addStockToProduct(scanner);
                case 19 -> displayOrdersOrderedByTotal();
                case 20 -> System.out.println("Thank you for using the E-Commerce Store. Goodbye!");
            }
        } while (choice != 20);
    }
}
