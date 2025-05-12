package bdavanzadas.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.RatingEntity;
import bdavanzadas.lab1.repositories.RatingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import bdavanzadas.lab1.repositories.UserRepository;
import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.services.UserService;



import java.util.List;


@Service
public class RatingService {

    @Autowired
    private final RatingRepository ratingRepository;
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;

    public RatingService(RatingRepository ratingRepository, JdbcTemplate jdbcTemplate) {
        this.ratingRepository = ratingRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<RatingEntity> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Transactional
    public void saveRating(RatingEntity rating) {
        // Verifica que el pedido exista
        String orderCheckSql = "SELECT COUNT(*) FROM orders WHERE id = ?";
        Integer orderCount = jdbcTemplate.queryForObject(orderCheckSql, Integer.class, rating.getOrderId());
        if (orderCount == null || orderCount == 0) {
            throw new IllegalArgumentException("El pedido con ID " + rating.getOrderId() + " no existe.");
        }

        // Verifica que el repartidor exista
        String dealerCheckSql = "SELECT COUNT(*) FROM dealers WHERE id = ?";
        Integer dealerCount = jdbcTemplate.queryForObject(dealerCheckSql, Integer.class, rating.getDealerId());
        if (dealerCount == null || dealerCount == 0) {
            throw new IllegalArgumentException("El repartidor con ID " + rating.getDealerId() + " no existe.");
        }

        // Guarda la calificación
        ratingRepository.save(rating);
    }
    @Transactional
    public void deleteRating(int id) {
        ratingRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public RatingEntity getRatingById(int id) {
        return ratingRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<RatingEntity> getRatingsByClientId(int clientId) {
        return ratingRepository.findByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public List<RatingEntity> getRatingsByDealerId(int dealerId) {
        return ratingRepository.findByDealerId(dealerId);
    }
    //dealer ratings a partir de un dealer autenticado

    @Transactional(readOnly = true)
    public List<RatingEntity> getRatingsByDealerIdAuthenticated() {
        try {
            // Obtener el userId del usuario autenticado
            Long userId = userService.getAuthenticatedUserId();
            if (userId == null) {
                throw new IllegalArgumentException("El usuario no está autenticado.");
            }

            // Obtener el dealerId a partir del userId
            String sql = "SELECT id FROM dealers WHERE user_id = ?";
            Integer dealerId = jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
            if (dealerId == null) {
                throw new IllegalArgumentException("El dealer no existe para el usuario autenticado.");
            }

            // Obtener las calificaciones a partir del dealerId
            return ratingRepository.findByDealerId(dealerId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("No se encontró un dealer asociado al usuario autenticado.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las calificaciones del dealer autenticado.", e);
        }
    }
}
