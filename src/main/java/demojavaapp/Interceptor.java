package demojavaapp;

import io.opentelemetry.api.trace.Span;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class Interceptor implements HandlerInterceptor {

    private final Database database;

    public Interceptor() {
        this.database = Database.INSTANCE;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String merchantId = request.getHeader("Merchant-ID");
        if (merchantId == null || merchantId.isBlank()) {
            return true;
        }
        final int id = Integer.parseInt(merchantId);
        final Merchant merchant = this.database.findMerchantByID(id)
            .orElseThrow();
        request.setAttribute("Merchant-ID", merchant.id());
        final Span span = Span.current();
        if (span != null) {
            span.setAttribute("limanyo.customer.id", merchant.id());
            span.setAttribute("limanyo.customer.name", merchant.name());
        }
        return true;
    }
}
