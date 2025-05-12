package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.RatingEntity;
import bdavanzadas.lab1.services.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@CrossOrigin(origins = "*") // Permite llamadas desde tu frontend Nuxt
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public ResponseEntity<List<RatingEntity>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRatings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingEntity> getRatingById(@PathVariable int id) {
        RatingEntity rating = ratingService.getRatingById(id);
        if (rating != null) {
            return ResponseEntity.ok(rating);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> addRating(@RequestBody RatingEntity rating) {
        ratingService.saveRating(rating);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRating(@PathVariable int id, @RequestBody RatingEntity rating) {
        rating.setId(id);
        ratingService.saveRating(rating);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable int id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RatingEntity>> getRatingsByClientId(@PathVariable int clientId) {
        List<RatingEntity> ratings = ratingService.getRatingsByClientId(clientId);
        if (ratings != null && !ratings.isEmpty()) {
            return ResponseEntity.ok(ratings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dealer/{dealerId}")
    public ResponseEntity<List<RatingEntity>> getRatingsByDealerId(@PathVariable int dealerId) {
        List<RatingEntity> ratings = ratingService.getRatingsByDealerId(dealerId);
        if (ratings != null && !ratings.isEmpty()) {
            return ResponseEntity.ok(ratings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dealer/ratings")
    public ResponseEntity<?> getRatingsByAuthenticatedDealer() {
        try {
            List<RatingEntity> ratings = ratingService.getRatingsByDealerIdAuthenticated();
            if (ratings != null && !ratings.isEmpty()) {
                return ResponseEntity.ok(ratings);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron calificaciones para el dealer autenticado.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al obtener las calificaciones.");
        }
    }




}
