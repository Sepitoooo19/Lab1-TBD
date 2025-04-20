package bdavanzadas.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.RatingEntity;
import bdavanzadas.lab1.repositories.RatingRepository;



import java.util.List;


@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Transactional(readOnly = true)
    public List<RatingEntity> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Transactional
    public void saveRating(RatingEntity rating) {
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
}
