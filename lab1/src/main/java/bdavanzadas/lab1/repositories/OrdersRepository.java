package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.OrdersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class OrdersRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<OrdersEntity> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getInt("products"),
                        rs.getDouble("total_price")
                )
        );
    }

    public void save(OrdersEntity order) {
        String sql = "INSERT INTO orders (order_date, delivery_date, status, client_id, products, total_price) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getOrderDate(), order.getDeliveryDate(), order.getStatus(), order.getClientId(), order.getProducts(), order.getTotalPrice());
    }

    public void update(OrdersEntity order) {
        String sql = "UPDATE orders SET order_date = ?, delivery_date = ?, status = ?, client_id = ?, products = ?, total_price = ? WHERE id = ?";
        jdbcTemplate.update(sql, order.getOrderDate(), order.getDeliveryDate(), order.getStatus(), order.getClientId(), order.getProducts(), order.getTotalPrice(), order.getId());
    }

    public void delete(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
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
                        rs.getInt("products"),
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
                        rs.getInt("products"),
                        rs.getDouble("total_price")
                )
        );
    }
}
