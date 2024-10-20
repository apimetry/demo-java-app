package demojavaapp;

import demojavaapp.io.PurchaseItem;
import demojavaapp.io.ShipmentMethod;

import java.util.HashSet;
import java.util.Set;

public class Order {
    public final int id;
    public final int merchantId;
    public Set<PurchaseItem> items;
    public ShipmentMethod shipment;

    public Order(int id, int merchantId, Set<PurchaseItem> lineItems) {
        this.id = id;
        this.merchantId = merchantId;
        this.items = new HashSet<>(lineItems);
    }
}
