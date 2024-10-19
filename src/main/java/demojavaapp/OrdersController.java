package demojavaapp;

import demojavaapp.io.CreateOrderResponse;
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
    public CreateOrderResponse create(@RequestHeader("Merchant-ID") int merchantId) {
        Order order = this.database.createOrder(merchantId);
        return new CreateOrderResponse(order.id, merchantId);
    }

    @PostMapping(path = "/{orderId}/ship")
    public void ship(@RequestHeader("Merchant-ID") int merchantId, @PathVariable("orderId") int orderId) {
        Order order = this.database.findOrder(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (order.shipped) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        order.shipped = true;
    }

}
