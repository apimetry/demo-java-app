package demojavaapp;

import io.opentelemetry.api.trace.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/orders")
public class OrdersController {

    private static final Logger log = LoggerFactory.getLogger(OrdersController.class);

    @PostMapping
    public String create() {
        return "created";
    }

    @PostMapping(path = "/{orderId}/ship")
    public String ship(@PathVariable("orderId") String orderId) {
        final Span span = Span.current();
        if (span != null) {
            span.setAttribute("limanyo.integrator.id", "1244");
            span.setAttribute("limanyo.flow.kind", "order");
            span.setAttribute("limanyo.flow.id", orderId);
        }
        return "shipped";
    }

}
