package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.DealerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DealerRepository  implements DealerRepositoryInt {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(DealerEntity dealer) {
        String sql = "INSERT INTO dealers (user_id, name, rut, email, phone, vehicle, plate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, dealer.getUserId(), dealer.getName(), dealer.getRut(), dealer.getEmail(), dealer.getPhone(), dealer.getVehicle(), dealer.getPlate());
    }

    public void update(DealerEntity dealer) {
        String sql = "UPDATE dealers SET name = ?, rut = ?, email = ?, phone = ?, vehicle = ?, plate = ? WHERE id = ?";
        jdbcTemplate.update(sql, dealer.getName(), dealer.getRut(), dealer.getEmail(), dealer.getPhone(), dealer.getVehicle(), dealer.getPlate(), dealer.getId());
    }
    public void delete(int id) {
        String sql = "DELETE FROM dealers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    public DealerEntity findById(int id) {
        String sql = "SELECT * FROM dealers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            DealerEntity dealer = new DealerEntity();
            dealer.setId(rs.getInt("id"));
            dealer.setName(rs.getString("name"));
            dealer.setRut(rs.getString("rut"));
            dealer.setEmail(rs.getString("email"));
            dealer.setPhone(rs.getString("phone"));
            dealer.setVehicle(rs.getString("vehicle"));
            dealer.setPlate(rs.getString("plate"));
            return dealer;
        });
    }


    public List<DealerEntity> findAll() {
        String sql = "SELECT * FROM dealers";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DealerEntity dealer = new DealerEntity();
            dealer.setId(rs.getInt("id"));
            dealer.setName(rs.getString("name"));
            dealer.setRut(rs.getString("rut"));
            dealer.setEmail(rs.getString("email"));
            dealer.setPhone(rs.getString("phone"));
            dealer.setVehicle(rs.getString("vehicle"));
            dealer.setPlate(rs.getString("plate"));
            return dealer;
        });
    }

    //RF 05 TOP 3 MEJORES REPARTIDORES
    public Float puntuacionProm(int id){
        String sql = "SELECT AVG(rating) FROM rating WHERE dealer_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, Float.class);
    }
}