package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientRepository implements ClientRepositoryInt {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(ClientEntity client) {
        String sql = "INSERT INTO clients (user_id, name, rut, email, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, client.getUserId(), client.getName(), client.getRut(), client.getEmail(), client.getPhone(), client.getAddress());


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
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            ClientEntity client = new ClientEntity();
            client.setId(rs.getInt("id"));
            client.setName(rs.getString("name"));
            client.setRut(rs.getString("rut"));
            client.setEmail(rs.getString("email"));
            client.setPhone(rs.getString("phone"));
            client.setAddress(rs.getString("address"));
            return client;
        });
    }

    public List<ClientEntity> findAll() {
        String sql = "SELECT * FROM clients";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ClientEntity client = new ClientEntity();
            client.setId(rs.getInt("id"));
            client.setName(rs.getString("name"));
            client.setRut(rs.getString("rut"));
            client.setEmail(rs.getString("email"));
            client.setPhone(rs.getString("phone"));
            client.setAddress(rs.getString("address"));
            return client;
        });
    }

    //getNameByClientId
    public String getNameByClientId(int clientId) {
        String sql = "SELECT name FROM clients WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{clientId}, String.class);
    }

}