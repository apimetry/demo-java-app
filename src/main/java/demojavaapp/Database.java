package demojavaapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import demojavaapp.io.PurchaseItem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;


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

    private static <T> T read(String fileName, Class<T> classDef, T defaultTo) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            String data = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
            if (data == null || data.isBlank()) {
                return defaultTo;
            }
            return JSON.readValue(data, classDef);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultTo;
        }
    }

    private static void write(String fileName, Object data) {
        try {
            File file = new File(fileName);
            file.createNewFile();
            var writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writer.write(JSON.writeValueAsString(data));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
