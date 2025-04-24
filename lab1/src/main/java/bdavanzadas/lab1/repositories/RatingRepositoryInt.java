package bdavanzadas.lab1.repositories;


import java.util.List;
import bdavanzadas.lab1.entities.RatingEntity;

public interface RatingRepositoryInt {

    List<RatingEntity> findAll();
    void save(RatingEntity rating);
    void update(RatingEntity rating);
    void delete(int id);
    RatingEntity findById(int id);
    List<RatingEntity> findByClientId(int clientId);
    List<RatingEntity> findByDealerId(int dealerId);
}
