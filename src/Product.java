public class Product implements Comparable<Product> {
    private final int id;
    private final String name;
    private double price;
    private final String category;
    private int stockQuantity;
    private static int nextId = 1;

    public Product(String name, double price, String category, int stockQuantity) {
        this.id = nextId++;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void removeStockQuantity(int quantity) {
        this.stockQuantity -= quantity;
    }

    public void addStockQuantity(int quantity) {
        this.stockQuantity += quantity;
    }

    @Override
    public int compareTo(Product other) {
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return name + " - " + price + " - " + category + " - " + stockQuantity;
    }
}
