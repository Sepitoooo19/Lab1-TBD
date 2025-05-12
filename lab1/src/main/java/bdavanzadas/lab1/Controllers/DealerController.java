package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.DealerEntity;
import bdavanzadas.lab1.services.DealerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dealers")
@CrossOrigin(origins = "*") // Permite llamadas desde tu frontend Nuxt
public class DealerController {

    private final DealerService dealerService;

    public DealerController(DealerService dealerService) {
        this.dealerService = dealerService;
    }

    @GetMapping
    public ResponseEntity<List<DealerEntity>> getAllDealers() {
        List<DealerEntity> dealers = dealerService.getAllDealers();
        return new ResponseEntity<>(dealers, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DealerEntity> getDealerById(@PathVariable int id) {
        DealerEntity dealer = dealerService.getDealerById(id);
        if (dealer != null) {
            return new ResponseEntity<>(dealer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<DealerEntity> createDealer(@RequestBody DealerEntity dealer) {
        dealerService.saveDealer(dealer);
        return new ResponseEntity<>(dealer, HttpStatus.CREATED);
    }
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

    @GetMapping("/average-delivery-time")
    public ResponseEntity<List<Map<String, Object>>> getAverageDeliveryTimeByDealer() {
        List<Map<String, Object>> result = dealerService.getAverageDeliveryTimeByDealer();
        return ResponseEntity.ok(result);
    }

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


}
