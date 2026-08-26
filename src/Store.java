import java.util.*;

public class Store {
    private List<Product> products;
    private Map<Integer, Product> productMap;
    private Map<Integer, Order> orders;
    private Set<String> categories;
    private Queue<Order> shippingQueue;
    private List<Order> deliveredOrders;
    private List<Review> reviews;

    public Store() {
        products = new ArrayList<>();
        productMap = new HashMap<>();
        orders = new HashMap<>();
        categories = new HashSet<>();
        shippingQueue = new ArrayDeque<>();
        deliveredOrders = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    public void addProduct(Scanner scanner) {
        String name = "";
        do {
            System.out.println("Enter the product's name: ");
            name = scanner.nextLine();
        } while (name.trim().isEmpty());

        double price = 1.0;
        do {
            if (price <= 0) {
                System.out.println("Price must be greater than 0");
            }
            System.out.println("Enter the product's price: ");
            try {
                price = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                price = 0.0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (price <= 0);

        String category = "";
        do {
            System.out.println("Enter the product's category");
            category = scanner.nextLine();
        } while (category.trim().isEmpty());
        String resultCategory = category.substring(0, 1).toUpperCase() + category.substring(1);

        int quantity = 1;
        do {
            if (quantity <= 0) {
                System.out.println("Stock Quantity must be greater than 0");
            }
            System.out.println("Enter the product's stock quantity: ");
            try {
                quantity = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                quantity = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (quantity <= 0);
        Product product = new Product(name, price, resultCategory, quantity);
        products.add(product);
        productMap.put(product.getId(), product);
        categories.add(resultCategory);
        System.out.println("Product added successfully.");
    }

    public void removeProduct(Scanner scanner) {
        int id = 1;
        do {
            if (id <= 0) {
                System.out.println("Product ID must be greater than 0.");
            }
            System.out.println("Enter the product ID: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                id = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (id <= 0);

        Product product = productMap.get(id);
        if (product == null) {
            System.out.println("Product not found.");
        } else {
            products.remove(product);
            productMap.remove(id);
            System.out.println("Product removed successfully.");
        }
    }

    public void displayProducts() {
        int index = 1;
        for (Product product : products) {
            System.out.println(index++ + " " + product);
        }
    }

    public void searchProduct(Scanner scanner) {
        int id = 1;
        do {
            if (id <= 0) {
                System.out.println("Product ID must be greater than 0.");
            }
            System.out.println("Enter the product ID: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                id = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (id <= 0);

        if (!productMap.containsKey(id)) {
            System.out.println("Product not found.");
        } else {
            System.out.println("Product found:");
            System.out.println(productMap.get(id));
        }
    }

    public void displayCategories() {
        System.out.println("Categories:");
        for (String category : categories) {
            System.out.println("\t- " + category);
        }
    }

    public void displayProductsByPrice() {
        ArrayList<Product> sortedProducts = new ArrayList<>(products);
        sortedProducts.sort(null);
        int index = 1;
        for (Product product : sortedProducts) {
            System.out.println(index++ + " " + product);
        }
    }

    public void createOrder(Scanner scanner) {
        String customerName = "";
        do {
            System.out.println("Enter the customer's name: ");
            customerName = scanner.nextLine();
        } while (customerName.trim().isEmpty());

        Order order = new Order(customerName);
        orders.put(order.getOrderId(), order);
        System.out.println("Order created successfully. Order ID: #" + order.getOrderId());
    }

    public void addItemToOrder(Scanner scanner) {
        int orderId = 1;
        do {
            if (orderId <= 0) {
                System.out.println("Order ID must be greater than 0.");
            }
            System.out.println("Enter the order ID: ");
            try {
                orderId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                orderId = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (orderId <= 0);

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Order is already " + order.getStatus() + ".");
        } else {
            int productId = 1;
            do {
                if (productId <= 0) {
                    System.out.println("Product ID must be greater than 0.");
                }
                System.out.println("Enter the product ID: ");
                try {
                    productId = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    productId = 0;
                    System.out.println("Invalid input. Please enter a number.");
                }
            } while (productId <= 0);

            Product product = productMap.get(productId);
            if (product == null) {
                System.out.println("Product not found.");
            } else {
                int quantity = 1;
                do {
                    if (quantity <= 0) {
                        System.out.println("Quantity must be greater than 0.");
                    }
                    System.out.println("Enter the quantity: ");
                    try {
                        quantity = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        quantity = 0;
                        System.out.println("Invalid input. Please enter a number.");
                    }
                } while (quantity <= 0);
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
        int orderId = 1;
        do {
            if (orderId <= 0) {
                System.out.println("Order ID must be greater than 0.");
            }
            System.out.println("Enter the order ID: ");
            try {
                orderId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                orderId = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (orderId <= 0);

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else if (order.getStatus() != OrderStatus.PENDING) {
                System.out.println("Order is already " + order.getStatus() + ".");
        } else {
            order.displayOrder();
            int itemId = 1;
            do {
                if (itemId <= 0) {
                    System.out.println("Item ID must be greater than 0.");
                }
                System.out.println("Enter the item ID: ");
                try {
                    itemId = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    itemId = 0;
                    System.out.println("Invalid input. Please enter a number.");
                }
            } while (itemId <= 0);

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
        int orderId = 1;
        do {
            if (orderId <= 0) {
                System.out.println("Order ID must be greater than 0.");
            }
            System.out.println("Enter the order ID: ");
            try {
                orderId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                orderId = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (orderId <= 0);

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else {
            order.displayOrder();
        }
    }

    public void addOrderToShippingList(Scanner scanner) {
        int orderId = 1;
        do {
            if (orderId <= 0) {
                System.out.println("Order ID must be greater than 0.");
            }
            System.out.println("Enter the order ID: ");
            try {
                orderId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                orderId = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (orderId <= 0);

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
        int orderId = 1;
        do {
            if (orderId <= 0) {
                System.out.println("Order ID must be greater than 0.");
            }
            System.out.println("Enter the order ID: ");
            try {
                orderId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                orderId = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (orderId <= 0);

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
        int orderId = 1;
        do {
            if (orderId <= 0) {
                System.out.println("Order ID must be greater than 0.");
            }
            System.out.println("Enter the order ID: ");
            try {
                orderId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                orderId = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (orderId <= 0);

        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found.");
        } else {
            order.displayOrder();
        }
    }

    public void addReview(Scanner scanner) {
        int productId = 1;
        do {
            if (productId <= 0) {
                System.out.println("Product ID must be greater than 0.");
            }
            System.out.println("Enter product ID: ");
            try {
                productId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                productId = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (productId <= 0);

        if(!productMap.containsKey(productId)) {
            System.out.println("Product not found.");
        } else {
            String customerName = "";
            do {
                System.out.println("Enter the customer's name: ");
                customerName = scanner.nextLine();
            } while (customerName.trim().isEmpty());

            int starRating = 1;
            do {
                if (starRating < 0 || starRating > 5) {
                    System.out.println("Star rating must be between 0 and 5.");
                }
                System.out.println("Enter a star rating: (0-5)");
                try {
                    starRating = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    starRating = -1;
                    System.out.println("Invalid input. Please enter a number.");
                }
            } while (starRating < 0 || starRating > 5);

            System.out.println("Enter your comment on the product: ");
            String comment = scanner.nextLine();
            Review review = new Review (productId, customerName, starRating, comment);
            reviews.add(review);
        }
    }

    public void displayReviewsForProduct(Scanner scanner) {
        int productId = 1;
        do {
            if (productId <= 0) {
                System.out.println("Product ID must be greater than 0.");
            }
            System.out.println("Enter product ID: ");
            try {
                productId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                productId = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (productId <= 0);

        for (Review review : reviews) {
            if (review.getProductId() == productId) {
                System.out.println(review);
            }
        }
        System.out.println("No reviews found for product ID " + productId + ".");
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
        int productId = 1;
        do {
            if (productId <= 0) {
                System.out.println("Product ID must be greater than 0.");
            }
            System.out.println("Enter the product's ID: ");
            try {
                productId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                productId = 0;
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (productId <= 0);

        if (!productMap.containsKey(productId)) {
            System.out.println("Product not found.");
        } else {
            int quantity = 1;
            do {
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                }
                System.out.println("Enter the quantity: ");
                try {
                    quantity = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    quantity = 0;
                    System.out.println("Invalid input. Please enter a number.");
                }
            } while (quantity <= 0);

            productMap.get(productId).addStockQuantity(quantity);
            System.out.println("Quantity added successfully.");
            System.out.println(productMap.get(productId));
        }
    }

    public void displayOrdersOrderedByTotal() {
        List<Order> orders = new ArrayList<>(this.orders.values());
        orders.sort(Comparator.comparingDouble(Order::getTotal));
        int index = 1;
        for (Order order : orders) {
            System.out.println(index++);
            order.displayOrder();
        }
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
        int choice = 1;
        Scanner scanner = new Scanner(System.in);
        do {
            printMenu();
            do {
                if (choice < 1 || choice > 20) {
                    System.out.println("Invalid choice. Please enter a number between 1 and 20.");
                }
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    choice = 0;
                    System.out.println("Invalid input. Please enter a number.");
                }
            } while (choice < 1 || choice > 20);

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
