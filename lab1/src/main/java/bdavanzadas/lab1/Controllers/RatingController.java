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

    // -----------------------------------------------------------------
    // CRUD BÁSICO
    // -----------------------------------------------------------------

    /** Devuelve todas las calificaciones. */
    @GetMapping
    public ResponseEntity<List<RatingEntity>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRatings());
    }

    /** Obtiene una calificación por su ID. */
    @GetMapping("/{id}")
    public ResponseEntity<RatingEntity> getRatingById(@PathVariable int id) {
        RatingEntity rating = ratingService.getRatingById(id);
        return (rating != null) ? ResponseEntity.ok(rating)
                : ResponseEntity.notFound().build();
    }

    /** Crea una nueva calificación. */
    @PostMapping
    public ResponseEntity<Void> addRating(@RequestBody RatingEntity rating) {
        ratingService.saveRating(rating);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Actualiza una calificación existente.
     * <p>Usa PUT para mantener la semántica REST.</p>
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRating(@PathVariable int id,
                                             @RequestBody RatingEntity rating) {
        rating.setId(id);
        ratingService.saveRating(rating);
        return ResponseEntity.noContent().build();
    }

    /** Elimina una calificación. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable int id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }

    // -----------------------------------------------------------------
    // CONSULTAS POR CLIENTE Y DEALER
    // -----------------------------------------------------------------

    /** Calificaciones realizadas por un cliente específico. */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RatingEntity>> getRatingsByClientId(@PathVariable int clientId) {
        List<RatingEntity> ratings = ratingService.getRatingsByClientId(clientId);
        return (ratings != null && !ratings.isEmpty())
                ? ResponseEntity.ok(ratings)
                : ResponseEntity.notFound().build();
    }

    /** Calificaciones recibidas por un dealer dado su ID. */
    @GetMapping("/dealer/{dealerId}")
    public ResponseEntity<List<RatingEntity>> getRatingsByDealerId(@PathVariable int dealerId) {
        List<RatingEntity> ratings = ratingService.getRatingsByDealerId(dealerId);
        return (ratings != null && !ratings.isEmpty())
                ? ResponseEntity.ok(ratings)
                : ResponseEntity.notFound().build();
    }

    // -----------------------------------------------------------------
    // DEALER AUTENTICADO
    // -----------------------------------------------------------------

    /**
     * Calificaciones asociadas al dealer actualmente autenticado.
     *
     * @return 200 OK con lista de ratings,
     *         404 si no hay registros,
     *         400 en error de validación,
     *         500 en error inesperado.
     */
    @GetMapping("/dealer/ratings")
    public ResponseEntity<?> getRatingsByAuthenticatedDealer() {
        try {
            List<RatingEntity> ratings = ratingService.getRatingsByDealerIdAuthenticated();
            if (ratings != null && !ratings.isEmpty()) {
                return ResponseEntity.ok(ratings);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron calificaciones para el dealer autenticado.");
            }
        } catch (IllegalArgumentException e) { // Error de validación (p.ej., usuario sin dealer)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {                // Error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al obtener las calificaciones.");
        }
    }

}
