package demojavaapp;

public class Order {
    public final int id;
    public final int merchantId;
    public boolean shipped = false;

    public Order(int id, int merchantId) {
        this.id = id;
        this.merchantId = merchantId;
    }
}
