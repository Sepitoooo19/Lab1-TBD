package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 *  La clase ClientRepository representa el repositorio de clientes en la base de datos.
 *  Esta clase contiene métodos para guardar, actualizar, eliminar y buscar clientes en la base de datos.
 *
 */
@Repository
public class ClientRepository implements ClientRepositoryInt {

    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * Metodo para guardar un cliente en la base de datos.
     * @param "client" El cliente a guardar.
     *
     */
    public void save(ClientEntity client) {
        String sql = "INSERT INTO clients (user_id, name, rut, email, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, client.getUserId(), client.getName(), client.getRut(), client.getEmail(), client.getPhone(), client.getAddress());


    }

    /**
     * Metodo para actualizar un cliente en la base de datos.
     * @param "client" El cliente a actualizar.
     *
     */
    public void update(ClientEntity client) {
        String sql = "UPDATE clients SET name = ?, rut = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        jdbcTemplate.update(sql, client.getName(), client.getRut(), client.getEmail(), client.getPhone(), client.getAddress(), client.getId());
    }


    /**
     * Metodo para eliminar un cliente de la base de datos.
     * @param "id" El id del cliente a eliminar.
     *
     */
    public void delete(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Metodo para buscar un cliente por su id.
     * @param "id" El id del cliente a buscar.
     * @return El cliente encontrado.
     *
     */
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

    /**
     * Metodo para buscar todos los clientes.
     * @return Una lista de todos los clientes.
     *
     */
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

    /**
     * Metodo para buscar un cliente por su rut.
     * @param "rut" El rut del cliente a buscar.
     * @return El cliente encontrado.
     *
     */
    //getNameByClientId
    public String getNameByClientId(int clientId) {
        String sql = "SELECT name FROM clients WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{clientId}, String.class);
    }

}