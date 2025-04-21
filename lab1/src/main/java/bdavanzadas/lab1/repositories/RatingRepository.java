package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.RatingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;



@Repository
public class RatingRepository implements RatingRepositoryInt {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RatingEntity> findAll() {
        String sql = "SELECT * FROM rating";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RatingEntity(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("date"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id")
                )
        );
    }

    public void save(RatingEntity rating) {
        String sql = "INSERT INTO rating (rating, comment, date, client_id, dealer_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, rating.getRating(), rating.getComment(), rating.getDate(), rating.getClientId(), rating.getDealerId());
    }

    public void update(RatingEntity rating) {
        String sql = "UPDATE rating SET rating = ?, comment = ?, date = ?, client_id = ?, dealer_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, rating.getRating(), rating.getComment(), rating.getDate(), rating.getClientId(), rating.getDealerId(), rating.getId());
    }

    public void delete(int id) {
        String sql = "DELETE FROM rating WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public RatingEntity findById(int id) {
        String sql = "SELECT * FROM rating WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new RatingEntity(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("date"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id")
                )
        );
    }

    public List<RatingEntity> findByClientId(int clientId) {
        String sql = "SELECT * FROM rating WHERE client_id = ?";
        return jdbcTemplate.query(sql, new Object[]{clientId}, (rs, rowNum) ->
                new RatingEntity(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("date"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id")
                )
        );
    }

    public List<RatingEntity> findByDealerId(int dealerId) {
        String sql = "SELECT * FROM rating WHERE dealer_id = ?";
        return jdbcTemplate.query(sql, new Object[]{dealerId}, (rs, rowNum) ->
                new RatingEntity(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("date"),
                        rs.getInt("client_id"),
                        rs.getInt("dealer_id")
                )
        );
    }
}
