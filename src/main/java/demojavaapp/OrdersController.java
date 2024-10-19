package demojavaapp;

import demojavaapp.io.CreateOrderRequest;
import demojavaapp.io.CreateOrderResponse;
import demojavaapp.io.GetOrderResponse;
import demojavaapp.io.ShipOrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/orders")
public class OrdersController {

    private final Database database;

    public OrdersController() {
        this.database = Database.INSTANCE;
    }

    @PostMapping
    public CreateOrderResponse create(
        @RequestHeader("Merchant-ID") int merchantId,
        CreateOrderRequest request
    ) {
        Order order = this.database.createOrder(merchantId, request.lineItems());
        return new CreateOrderResponse(order.id, merchantId);
    }

    @GetMapping(path = "/{orderId}")
    public GetOrderResponse getOrder(
        @RequestHeader("Merchant-ID") int merchantId,
        @PathVariable("orderId") int orderId
    ) {
        Order order = this.database.findOrder(orderId, merchantId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new GetOrderResponse(order.id, order.merchantId, order.lineItems, order.shipment);
    }

    @PostMapping(path = "/{orderId}/ship")
    public void ship(
        @RequestHeader("Merchant-ID") int merchantId,
        @PathVariable("orderId") int orderId,
        ShipOrderRequest request
    ) {
        Order order = this.database.findOrder(orderId, merchantId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (order.shipment != null)  {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        order.shipment = request.method();
    }

}
