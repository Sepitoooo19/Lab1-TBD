package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.DealerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DealerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DealerEntity> findAll() {
        String sql = "SELECT * FROM dealers";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new DealerEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("rut"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("vehicle"),
                        rs.getString("plate")
                )
        );
    }

    public void save(DealerEntity dealer) {
        String sql = "INSERT INTO dealers (name, rut, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, dealer.getName(), dealer.getRut(), dealer.getEmail(), dealer.getPhone());
    }

    public void update(DealerEntity dealer) {
        String sql = "UPDATE dealers SET name = ?, rut = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        jdbcTemplate.update(sql, dealer.getName(), dealer.getRut(), dealer.getEmail(), dealer.getPhone(), dealer.getId());
    }

    public void delete(int id) {
        String sql = "DELETE FROM dealers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public DealerEntity findById(int id) {
        String sql = "SELECT * FROM dealers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new DealerEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("rut"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("vehicle"),
                        rs.getString("plate")
                )
        );
    }

    public DealerEntity findByRut(String rut) {
        String sql = "SELECT * FROM dealers WHERE rut = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{rut}, (rs, rowNum) ->
                new DealerEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("rut"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("vehicle"),
                        rs.getString("plate")
                )
        );
    }

}