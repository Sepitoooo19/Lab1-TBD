package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.dtos.TopSpenderDTO;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.entities.OrdersEntity;
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

    public List<OrdersEntity> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new OrdersEntity(
                        rs.getInt("id"),
                        rs.getDate("order_date"),
                        rs.getDate("delivery_date"),
                        rs.getString("status"),
                        rs.getInt("client_id"),
                        rs.getDouble("total_price")
                )
        );
    }

    public void save(OrdersEntity order) {
        String sql = "INSERT INTO orders (order_date, delivery_date, status, client_id, , total_price) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getOrderDate(), order.getDeliveryDate(), order.getStatus(), order.getClientId(), order.getTotalPrice());
    }

    public void update(OrdersEntity order) {
        String sql = "UPDATE orders SET order_date = ?, delivery_date = ?, status = ?, client_id = ?, total_price = ? WHERE id = ?";
        jdbcTemplate.update(sql, order.getOrderDate(), order.getDeliveryDate(), order.getStatus(), order.getClientId(), order.getTotalPrice(), order.getId());
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
                        rs.getInt("products"),
                        rs.getDouble("total_price")
                )
        );
    }

    public TopSpenderDTO getTopSpender() {
        String sql = """
        SELECT c.id, c.name, c.rut, c.email, c.phone, c.address, SUM(o.total_price) AS total_spent
        FROM orders o
        JOIN clients c ON o.client_id = c.id
        WHERE o.status = 'entregado'
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

}
