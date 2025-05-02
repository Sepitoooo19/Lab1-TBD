package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public UserEntity findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) ->
                    new UserEntity(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    ));
        } catch (EmptyResultDataAccessException e) {
            return null; // Retorna null si no encuentra el usuario
        }
    }

    public void save(UserEntity user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            // La clave importante aquí es "Statement.RETURN_GENERATED_KEYS"
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            return ps;
        }, keyHolder);

        // Aquí obtenemos el ID generado con mayor precisión
        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("id")) {
            user.setId(((Number) keys.get("id")).intValue());
        } else {
            throw new IllegalStateException("No se pudo generar el ID para el usuario");
        }
    }
}