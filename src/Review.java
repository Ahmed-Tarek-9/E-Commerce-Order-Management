public class Review {
    private int productId;
    private String customerName;
    private int starRating;
    private String comment;

    public Review(int productId, String customerName, int starRating, String comment) {
        this.productId = productId;
        this.customerName = customerName;
        this.starRating = starRating;
        this.comment = comment;
    }

    public int getProductId() {
        return productId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getStarRating() {
        return starRating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Review:\n\tProduct ID: " + productId + "\n\tCustomer Name: " + customerName
                + "\n\tStar Rating: " + starRating + " / 5" + "\n\tComment: \"" + comment + "\"";
    }
}
