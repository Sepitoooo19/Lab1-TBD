package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.PaymentMethodEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentMethodRepostitory {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //FIND ALL
    public List<PaymentMethodEntity> findAll() {
        String sql = "SELECT * FROM payment_method";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new PaymentMethodEntity(
                        rs.getInt("id"),
                        rs.getString("type")));
    }
    //FIND BY ID
    public PaymentMethodEntity findById(int id) {
        String sql = "SELECT * FROM payment_method WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            PaymentMethodEntity p = new PaymentMethodEntity();
            p.setId(rs.getInt("id"));
            p.setType(rs.getString("type"));
            return p;
        });
    }
    //FIND BY NAME
    public PaymentMethodEntity findByType(String type) {
        String sql = "SELECT * FROM payment_method WHERE type = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{type}, (rs, rowNum) -> {
            PaymentMethodEntity p = new PaymentMethodEntity();
            p.setId(rs.getInt("id"));
            p.setType(rs.getString("type"));
            return p;
        });
    }

    // SAVE
    public void save(PaymentMethodEntity p) {
        String sql = "INSERT INTO payment_method (type) VALUES (?)";
        jdbcTemplate.update(sql, p.getType());
    }

    //UPDATE
    public void update(PaymentMethodEntity p) {
        String sql = "UPDATE payment_method SET type = ? WHERE id = ?";
        jdbcTemplate.update(sql, p.getType(), p.getId());
    }

    //DELETE
    public void deleteById(int id) {
        String sql = "DELETE FROM payment_method WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


}

