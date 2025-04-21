package bdavanzadas.lab1.repositories;
import bdavanzadas.lab1.entities.OrderDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;





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
        String sql = "INSERT INTO order_details (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderDetails.getOrderId(), orderDetails.getPrice());
    }

    public void update(OrderDetailsEntity orderDetails) {
        String sql = "UPDATE order_details SET order_id = ?, product_id = ?, quantity = ?, price = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderDetails.getOrderId(), orderDetails.getPrice(), orderDetails.getId());
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
}
