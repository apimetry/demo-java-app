package demojavaapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Database {

    public static final Database INSTANCE = new Database();

    private final AtomicInteger sequence = new AtomicInteger();
    private final List<Merchant> merchants = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();


    public Merchant createMerchant(String name) {
        Merchant merchant =  new Merchant(sequence.incrementAndGet(), name);
        this.merchants.add(merchant);
        return merchant;
    }

    public Optional<Merchant> findMerchantByID(int id) {
        return this.merchants.stream()
            .filter((merchant) -> merchant.id() == id)
            .findFirst();
    }

    public Order createOrder(int merchantId) {
        Order order = new Order(sequence.incrementAndGet(), merchantId);
        orders.add(order);
        return order;
    }

    public Optional<Order> findOrder(int id) {
        return this.orders.stream()
            .filter((order) -> order.id == id)
            .findFirst();
    }

    public List<Order> findMerchantOrders(int merchantId) {
        return this.orders.stream()
            .filter((order) -> order.merchantId == merchantId)
            .toList();
    }
}
