package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class ClientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ClientEntity> findAll() {
        String sql = "SELECT * FROM clients";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ClientEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("rut"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                )
        );
    }

    public void save(ClientEntity client) {
        String sql = "INSERT INTO clients (name, rut, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, client.getName(), client.getRut(), client.getEmail(), client.getPhone(), client.getAddress());
    }

    public void update(ClientEntity client) {
        String sql = "UPDATE clients SET name = ?, rut = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        jdbcTemplate.update(sql, client.getName(), client.getRut(), client.getEmail(), client.getPhone(), client.getAddress(), client.getId());
    }

    public void delete(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    public ClientEntity findById(int id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new ClientEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("rut"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                )
        );
    }
}
