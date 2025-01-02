package demojavaapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import demojavaapp.io.PurchaseItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

import static demojavaapp.Utils.readf;

public class Database {

    private record ListOfMerchants(List<Merchant> list) {
        public static ListOfMerchants EMPTY = new ListOfMerchants(new ArrayList<>());
    }
    private record ListOfOrders(List<Order> list) {
        public static ListOfOrders EMPTY = new ListOfOrders(new ArrayList<>());
    }

    public static final Database INSTANCE = new Database();

    private static final String FILE_MERCHANTS = "/app/data/merchants.json";
    private static final String FILE_ORDERS = "/app/data/orders.json";

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final ObjectMapper JSON = new ObjectMapper();


    public Merchant createMerchant(String name) {
        Merchant merchant = new Merchant(RANDOM.nextInt(), name, UUID.randomUUID().toString());
        ListOfMerchants merchants = read(FILE_MERCHANTS, ListOfMerchants.class, ListOfMerchants.EMPTY);
        merchants.list.add(merchant);
        write(FILE_MERCHANTS, merchants);
        return merchant;
    }

    public Optional<Merchant> findMerchantByID(int id) {
        return read(FILE_MERCHANTS, ListOfMerchants.class, ListOfMerchants.EMPTY)
            .list()
            .stream()
            .filter((merchant) -> merchant.id() == id)
            .findFirst();
    }

    public Optional<Merchant> findMerchantByAuthToken(String authToken) {
        return read(FILE_MERCHANTS, ListOfMerchants.class, ListOfMerchants.EMPTY)
            .list()
            .stream()
            .filter((merchant) -> merchant.authToken().equals(authToken))
            .findFirst();
    }

    public Order createOrder(int merchantId, List<PurchaseItem> lineItems) {
        Order order = new Order(RANDOM.nextInt(), merchantId, lineItems);
        ListOfOrders orders = read(FILE_ORDERS, ListOfOrders.class, ListOfOrders.EMPTY);
        orders.list.add(order);
        write(FILE_ORDERS, orders);
        return order;
    }

    public Optional<Order> findOrder(int id) {
        return read(FILE_ORDERS, ListOfOrders.class, ListOfOrders.EMPTY)
            .list()
            .stream()
            .filter((order) -> order.id == id)
            .findFirst();
    }

    public Optional<Order> findOrder(int id, long merchantId) {
        return read(FILE_ORDERS, ListOfOrders.class, ListOfOrders.EMPTY)
            .list()
            .stream()
            .filter((order) -> order.id == id && order.merchantId == merchantId)
            .findFirst();
    }

    public List<Order> findMerchantOrders(int merchantId) {
        return read(FILE_ORDERS, ListOfOrders.class, ListOfOrders.EMPTY)
            .list()
            .stream()
            .filter((order) -> order.merchantId == merchantId)
            .toList();
    }

    private static <T> T read(String file, Class<T> classDef, T defaultTo) {
        try {
            return JSON.readValue(readf(file), classDef);
        } catch (Exception e) {
            return defaultTo;
        }
    }

    private static void write(String fileName, Object data) {
        try {
            File file = new File(fileName);
            file.createNewFile();
            var writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writer.write(JSON.writeValueAsString(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
