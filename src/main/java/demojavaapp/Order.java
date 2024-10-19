package demojavaapp;

import demojavaapp.io.ShipmentMethod;

import java.util.HashSet;
import java.util.Set;

public class Order {
    public final int id;
    public final int merchantId;
    public Set<String> lineItems;
    public ShipmentMethod shipment;

    public Order(int id, int merchantId, Set<String> lineItems) {
        this.id = id;
        this.merchantId = merchantId;
        this.lineItems = new HashSet<>(lineItems);
    }
}
