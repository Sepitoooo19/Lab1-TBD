package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.DealerEntity;
import bdavanzadas.lab1.services.DealerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 *
 * La clase DealerController maneja las solicitudes relacionadas con los dealers.
 * Esta clase contiene métodos para obtener, crear, actualizar y eliminar dealers en la base de datos.
 *
 * */
@RestController
@RequestMapping("/dealers")
@CrossOrigin(origins = "*") // Permite llamadas desde tu frontend Nuxt
public class DealerController {


    /**
     *
     * Servicio de dealers.
     * Este servicio se utiliza para interactuar con la base de datos de dealers.
     *
     * */
    private final DealerService dealerService;


    /**
     *
     * Constructor de la clase DealerController.
     * @param "dealerService" El servicio de dealers a utilizar.
     *
     * */
    public DealerController(DealerService dealerService) {
        this.dealerService = dealerService;
    }



    /**
     *
     * Endpoint para obtener todos los dealers.
     * Este endpoint devuelve una lista de todos los dealers en la base de datos.
     *
     * */
    @GetMapping
    public ResponseEntity<List<DealerEntity>> getAllDealers() {
        List<DealerEntity> dealers = dealerService.getAllDealers();
        return new ResponseEntity<>(dealers, HttpStatus.OK);
    }

    /**
     *
     * Endpoint para obtener un dealer por su ID.
     * Este endpoint devuelve un dealer específico basado en su ID.
     *
     * */
    @GetMapping("/{id}")
    public ResponseEntity<DealerEntity> getDealerById(@PathVariable int id) {
        DealerEntity dealer = dealerService.getDealerById(id);
        if (dealer != null) {
            return new ResponseEntity<>(dealer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * Endpoint para crear un nuevo dealer.
     * Este endpoint guarda un nuevo dealer en la base de datos.
     *
     * */
    @PostMapping
    public ResponseEntity<DealerEntity> createDealer(@RequestBody DealerEntity dealer) {
        dealerService.saveDealer(dealer);
        return new ResponseEntity<>(dealer, HttpStatus.CREATED);
    }

    /**
     *
     * Endpoint para actualizar un dealer existente.
     * Este endpoint actualiza un dealer existente en la base de datos.
     *
     * */
    @PutMapping("/{id}")
    public ResponseEntity<DealerEntity> updateDealer(@PathVariable int id, @RequestBody DealerEntity dealer) {
        DealerEntity existingDealer = dealerService.getDealerById(id);
        if (existingDealer != null) {
            dealer.setId(id);
            dealerService.updateDealer(dealer);
            return new ResponseEntity<>(dealer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     *
     * Endpoint para eliminar un dealer.
     * Este endpoint elimina un dealer específico basado en su ID.
     *
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDealer(@PathVariable int id) {
        DealerEntity existingDealer = dealerService.getDealerById(id);
        if (existingDealer != null) {
            dealerService.deleteDealer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Calcula el tiempo promedio de entrega por dealer.
     *
     * @return Lista de mapas con {dealer_id, dealer_name, average_delivery_time}.
     */
    @GetMapping("/average-delivery-time")
    public ResponseEntity<List<Map<String, Object>>> getAverageDeliveryTimeByDealer() {
        List<Map<String, Object>> result = dealerService.getAverageDeliveryTimeByDealer();
        return ResponseEntity.ok(result);
    }
    /**
     * Obtiene el nombre de un dealer a partir de su ID.
     *
     * @param "dealerId" identificador.
     * @return 200 OK con el nombre o 404 si no existe (propagado por servicio).
     */
    @GetMapping("/{id}/name")
    public ResponseEntity<String> getDealerNameById(@PathVariable("id") Integer dealerId) {
        String dealerName = dealerService.getDealerNameById(dealerId);
        return ResponseEntity.ok(dealerName);
    }

    @GetMapping("/top-performers")
    public ResponseEntity<List<Map<String, Object>>> getTopPerformingDealers() {
        List<Map<String, Object>> topDealers = dealerService.getTopPerformingDealers();
        return ResponseEntity.ok(topDealers);
    }
    /**
     * Devuelve los dealers con mejor desempeño (por ejemplo,
     * menor tiempo de entrega o mayor número de pedidos).
     *
     * @return Lista de dealers destacados.
     */
    @GetMapping("/average-delivery-time-authenticated")
    public ResponseEntity<Double> getAverageDeliveryTimeByAuthenticatedDealer() {
        try {
            Double avgTime = dealerService.getAverageDeliveryTimeByAuthenticatedDealer();
            return ResponseEntity.ok(avgTime != null ? avgTime : 0.0);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    /**
     * Tiempo promedio de entrega para el dealer actualmente autenticado.
     * Se asume que el Dealer está vinculado al usuario autenticado (por
     * ejemplo, mediante Spring Security + UserDetails).
     *
     * @return 200 OK con el promedio, 403 si no autorizado, 500 en error.
     */
    @GetMapping("/delivery-count")
    public ResponseEntity<Integer> getDeliveryCountByAuthenticatedDealer() {
        try {
            Integer deliveryCount = dealerService.getDeliveryCountByAuthenticatedDealer();
            return ResponseEntity.ok(deliveryCount != null ? deliveryCount : 0);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Error de validación
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Error inesperado
        }
    }

    /**
     * Obtiene todos los datos del repartidor autenticado
     * @return Datos consolidados del repartidor
     */
    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedDealerProfile() {
        try {
            Map<String, Object> dealerData = dealerService.getAuthenticatedDealerData();

            if (dealerData == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No se encontró perfil de repartidor para el usuario autenticado"));
            }

            return ResponseEntity.ok(dealerData);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "No autorizado", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "details", e.getMessage()));
        }
    }


}
