package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.DealerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    //RF 04: tiempo promedio entre entrega y pedido por repartidor
    public List<Map<String, Object>> getAverageDeliveryTimeByDealer() {
        String sql = """
            SELECT 
                d.id AS dealer_id,
                d.name AS dealer_name,
                AVG(EXTRACT(EPOCH FROM (o.delivery_date - o.order_date)) / 3600) AS avg_delivery_time_hours
            FROM 
                orders o
            JOIN 
                dealers d ON o.dealer_id = d.id
            WHERE 
                o.status = 'ENTREGADO' AND o.delivery_date IS NOT NULL
            GROUP BY 
                d.id, d.name
            ORDER BY 
                avg_delivery_time_hours ASC;
        """;

        return jdbcTemplate.queryForList(sql);
    }

    public String findDealerNameById(int dealerId) {
        String sql = "SELECT name FROM dealers WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{dealerId}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "Sin asignar"; // Si no se encuentra el dealer, devuelve "Sin asignar"
        }
    }

    //RF 05: tres mejores repartidores
    public List<Map<String, Object>> getTopPerformingDealers() {
        String sql = """
            SELECT 
                d.id AS dealer_id,
                d.name AS dealer_name,
                COUNT(o.id) AS total_deliveries,
                COALESCE(AVG(r.rating), 0) AS avg_rating,
                (COUNT(o.id) * 0.7 + COALESCE(AVG(r.rating), 0) * 0.3) AS performance_score
            FROM 
                dealers d
            LEFT JOIN 
                orders o ON d.id = o.dealer_id AND o.status = 'ENTREGADO'
            LEFT JOIN 
                ratings r ON d.id = r.dealer_id
            GROUP BY 
                d.id, d.name
            ORDER BY 
                performance_score DESC
            LIMIT 3;
        """;

        return jdbcTemplate.queryForList(sql);
    }

    public Double getAverageDeliveryTimeByAuthenticatedDealer(Long userId) {
        // Obtener el dealerId asociado al usuario autenticado
        String sqlDealerId = "SELECT id FROM dealers WHERE user_id = ?";
        Integer dealerId = jdbcTemplate.queryForObject(sqlDealerId, new Object[]{userId}, Integer.class);

        if (dealerId == null) {
            throw new IllegalArgumentException("No se encontró un repartidor asociado al usuario autenticado.");
        }

        // Calcular el tiempo promedio de espera para el dealerId
        String sqlAvgTime = """
            SELECT AVG(EXTRACT(EPOCH FROM (o.delivery_date - o.order_date)) / 3600) AS avg_delivery_time_hours
            FROM orders o
            WHERE o.dealer_id = ? AND o.status = 'ENTREGADO' AND o.delivery_date IS NOT NULL
        """;

        return jdbcTemplate.queryForObject(sqlAvgTime, new Object[]{dealerId}, Double.class);
    }

    public Integer getDeliveryCountByAuthenticatedDealer(Long userId) {
        // Obtener el dealerId asociado al usuario autenticado
        String sqlDealerId = "SELECT id FROM dealers WHERE user_id = ?";
        Integer dealerId = jdbcTemplate.queryForObject(sqlDealerId, new Object[]{userId}, Integer.class);

        if (dealerId == null) {
            throw new IllegalArgumentException("No se encontró un repartidor asociado al usuario autenticado.");
        }

        // Contar las entregas realizadas por el dealerId
        String sqlDeliveryCount = """
            SELECT COUNT(*) 
            FROM orders 
            WHERE dealer_id = ? AND status = 'ENTREGADO'
        """;

        return jdbcTemplate.queryForObject(sqlDeliveryCount, new Object[]{dealerId}, Integer.class);
    }




}