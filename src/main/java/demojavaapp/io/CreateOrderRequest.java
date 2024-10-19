package demojavaapp.io;

import java.util.Set;

public record CreateOrderRequest(Set<String> lineItems) {
}
