package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.dtos.TopSpenderDTO;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.entities.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class OrdersRepository implements OrdersRepositoryInt {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DealerRepository dealerRepository;

    public List<OrdersEntity> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getDouble("total_price")
                )
        );
    }

    public void save(OrdersEntity order) {
        String sql = "INSERT INTO orders (order_date, delivery_date, status, client_id,dealer_id , total_price) VALUES (?, ?, ?, ?, ?,?)";
        jdbcTemplate.update(sql, order.getOrderDate(), order.getDeliveryDate(), order.getStatus(), order.getClientId(),order.getDealerId() ,order.getTotalPrice());
    }

    // Método para guardar la relación entre la orden y los productos
    public void saveOrderProducts(int orderId, List<Integer> productIds) {
        String sql = "INSERT INTO order_products (order_id, product_id) VALUES (?, ?)";

        // Insertar la relación entre la orden y los productos utilizando los IDs
        for (Integer productId : productIds) {
            jdbcTemplate.update(sql, orderId, productId);
        }
    }

    public void update(OrdersEntity order) {
        String sql = "UPDATE orders SET order_date = ?, delivery_date = ?, status = ?, client_id = ?,dealer_id= ?, total_price = ? WHERE id = ?";
        jdbcTemplate.update(sql, order.getOrderDate(), order.getDeliveryDate(), order.getStatus(), order.getClientId(),order.getDealerId(), order.getTotalPrice(), order.getId());
    }

    public void delete(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Método para obtener el último ID insertado (order_id)
    public int getLastInsertedOrderId() {
        String sql = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public OrdersEntity findById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getDouble("total_price")
                )
        );
    }

    public List<OrdersEntity> findByClientId(int clientId) {
        String sql = "SELECT * FROM orders WHERE client_id = ?";
        return jdbcTemplate.query(sql, new Object[]{clientId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getDouble("total_price")
                )
        );
    }

    public List<OrdersEntity> findByDealerId(int dealerId) {
        String sql = "SELECT * FROM orders WHERE dealer_id = ?";
        return jdbcTemplate.query(sql, new Object[]{dealerId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getDouble("total_price")
                )
        );
    }


    //RF 1: obtener el cliente que más ha gastado
    public TopSpenderDTO getTopSpender() {
        String sql = """
        SELECT c.id, c.name, c.rut, c.email, c.phone, c.address, SUM(o.total_price) AS total_spent
        FROM orders o
        JOIN clients c ON o.client_id = c.id
        WHERE o.status = 'ENTREGADO'
        GROUP BY c.id, c.name, c.rut, c.email, c.phone, c.address
        ORDER BY total_spent DESC
        LIMIT 1
    """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            TopSpenderDTO topSpender = new TopSpenderDTO();
            topSpender.setId(rs.getInt("id"));
            topSpender.setName(rs.getString("name"));
            topSpender.setRut(rs.getString("rut"));
            topSpender.setEmail(rs.getString("email"));
            topSpender.setPhone(rs.getString("phone"));
            topSpender.setAddress(rs.getString("address"));
            topSpender.setTotalSpent(rs.getDouble("total_spent"));
            return topSpender;
        });
    }
    public List<OrdersEntity> findOrdersByMonth(int month, int year) {
        String sql = "SELECT * FROM orders WHERE EXTRACT(MONTH FROM order_date) = ? AND EXTRACT(YEAR FROM order_date) = ?";
        return jdbcTemplate.query(sql, new Object[]{month, year}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getDouble("total_price")
                )
        );
    }


    public List<OrdersEntity> findFailedOrdersByCompanyId(int companyId) {
        String sql = """
        SELECT 
            o.id,
            o.order_date,
            o.delivery_date,
            o.status,
            o.client_id,
            o.dealer_id,
            o.total_price
        FROM orders o
        JOIN order_products op ON o.id = op.order_id
        JOIN products p ON op.product_id = p.id
        JOIN companies c ON p.company_id = c.id
        WHERE c.id = ? AND o.status = 'FALLIDA'
    """;
        return jdbcTemplate.query(sql, new Object[]{companyId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getDouble("total_price")
                )
        );
    }



    public List<OrdersEntity> findDeliveredOrdersByCompanyId(int companyId) {
        String sql = """
        SELECT 
            o.id,
            o.order_date,
            o.delivery_date,
            o.status,
            o.client_id,
            o.dealer_id,
            o.total_price
        FROM orders o
        JOIN order_products op ON o.id = op.order_id
        JOIN products p ON op.product_id = p.id
        JOIN companies c ON p.company_id = c.id
        WHERE c.id = ? AND o.status = 'ENTREGADO'
    """;
        return jdbcTemplate.query(sql, new Object[]{companyId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getDouble("total_price")
                )
        );
    }

    public void updateOrderStatusByDealerId(int orderId, int dealerId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ? AND dealer_id = ?";
        jdbcTemplate.update(sql, newStatus, orderId, dealerId);
    }

    //get all orders by company id
    public List<OrdersEntity> findOrdersByCompanyId(int companyId) {
        String sql = """
        SELECT o.*
        FROM orders o
        JOIN dealers d ON o.dealer_id = d.id
        JOIN products p ON d.id = p.company_id
        WHERE p.company_id = ?
    """;
        return jdbcTemplate.query(sql, new Object[]{companyId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getDouble("total_price")
                )
        );
    }

    //Get all products by order id
    public List<ProductEntity> findProductsByOrderId(int orderId) {
        String sql = "SELECT p.* FROM products p JOIN order_products op ON p.id = op.product_id WHERE op.order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) ->
                new ProductEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getFloat("price"),
                        rs.getString("category"),
                        rs.getInt("company_id")
                )
        );
    }

    //GET ID OF ORDER
    public int getOrderIdByClientId(int clientId) {
        String sql = "SELECT id FROM orders WHERE client_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{clientId}, Integer.class);
    }

    // Obtener la orden En proceso por ID del repartidor
    public OrdersEntity findActiveOrderByDealerId(int dealerId) {
        String sql = "SELECT * FROM orders WHERE dealer_id = ? AND status = 'EN PROCESO'";
        List<OrdersEntity> orders = jdbcTemplate.query(sql, new Object[]{dealerId}, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id"),
                        rs.getDouble("total_price")
                )
        );
        return orders.isEmpty() ? null : orders.get(0);
    }

    public void assignOrderToDealer(int orderId, int dealerId) {
        String sql = "UPDATE orders SET dealer_id = ?, status = 'EN PROCESO' WHERE id = ?";
        jdbcTemplate.update(sql, dealerId, orderId);
    }

    public void markAsDelivered(int orderId, String deliveryDate) {
        String sql = "UPDATE orders SET status = 'ENTREGADO', delivery_date = ? WHERE id = ?";
        jdbcTemplate.update(sql, deliveryDate, orderId);
    }

    public void markAsFailed(int orderId) {
        String sql = "UPDATE orders SET status = 'FALLIDA' WHERE id = ?";
        jdbcTemplate.update(sql, orderId);
    }





}
