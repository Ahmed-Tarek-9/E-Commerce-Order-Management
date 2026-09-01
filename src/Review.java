public record Review (int productId, String customerName, int starRating, String comment) {

    @Override
    public String toString() {
        return "Review:\n\tProduct ID: " + productId + "\n\tCustomer Name: " + customerName
                + "\n\tStar Rating: " + starRating + " / 5" + "\n\tComment: \"" + comment + "\"";
    }
}
