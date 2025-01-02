package demojavaapp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

public class Interceptor implements HandlerInterceptor {

    private final Database database;

    public Interceptor() {
        this.database = Database.INSTANCE;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String id = request.getHeader("Merchant-ID");
        if (id == null || id.isBlank()) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getMethodAnnotation(NoAuth.class) != null) {
                return true;
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        final Merchant merchant = this.database.findMerchantByID(Integer.parseInt(id))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        request.setAttribute("Merchant-ID", merchant.id());
        return true;
    }
}
