package demojavaapp.io;

import java.util.Set;

public record GetOrderResponse(
    int id,
    int merchantId,
    Set<PurchaseItem> lineItems,
    ShipmentMethod shipment
) {
}
