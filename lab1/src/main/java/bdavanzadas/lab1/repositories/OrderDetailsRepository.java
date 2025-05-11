package bdavanzadas.lab1.repositories;
import bdavanzadas.lab1.entities.OrderDetailsEntity;
import bdavanzadas.lab1.entities.PaymentMethodEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class OrderDetailsRepository implements OrderDetailsRepositoryInt {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<OrderDetailsEntity> findAll()  {
        String sql = "SELECT * FROM order_details";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new OrderDetailsEntity(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getString("payment_method"),
                        rs.getInt("total_products"),
                        rs.getDouble("price")
                )
        );
    }

    public void save(OrderDetailsEntity orderDetails) {
        String sql = "INSERT INTO order_details (order_id, payment_method, total_products, price) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderDetails.getOrderId(), orderDetails.getPaymentMethod(), orderDetails.getTotalProducts(), orderDetails.getPrice());
    }

    public void update(OrderDetailsEntity orderDetails) {
        String sql = "UPDATE order_details SET order_id = ?, payment_method = ?, total_products = ?, price = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderDetails.getOrderId(), orderDetails.getPaymentMethod(), orderDetails.getTotalProducts(), orderDetails.getPrice());
    }

    public void delete(int id) {
        String sql = "DELETE FROM order_details WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public OrderDetailsEntity findById(int id) {
        String sql = "SELECT * FROM order_details WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new OrderDetailsEntity(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getString("payment_method"),
                        rs.getInt("total_products"),
                        rs.getDouble("price")
                )
        );
    }

    public List<OrderDetailsEntity> findByOrderId(int orderId) {
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) ->
                new OrderDetailsEntity(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getString("payment_method"),
                        rs.getInt("total_products"),
                        rs.getDouble("price")
                )
        );
    }

    //RF 06: medio de pago en pedidos urgentes
    public Map<String, Integer> getMostUsedPaymentMethodForUrgentOrders() {
        String sql = """
            SELECT 
                od.payment_method, 
                COUNT(*) AS usage_count
            FROM 
                orders o
            JOIN 
                order_details od ON o.id = od.order_id
            WHERE 
                o.status = 'URGENTE'
            GROUP BY 
                od.payment_method
            ORDER BY 
                usage_count DESC
            LIMIT 1;
        """;

        return jdbcTemplate.query(sql, rs -> {
            Map<String, Integer> result = new HashMap<>();
            if (rs.next()) {
                result.put(rs.getString("payment_method"), rs.getInt("usage_count"));
            }
            return result;
        });
    }


}
