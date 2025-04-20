package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.DealerEntity;
import bdavanzadas.lab1.services.DealerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(dealerService.getAllDealers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealerEntity> getDealerById(@PathVariable int id) {
        DealerEntity dealer = dealerService.getDealerById(id);
        if (dealer != null) {
            return ResponseEntity.ok(dealer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> addDealer(@RequestBody DealerEntity dealer) {
        dealerService.saveDealer(dealer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDealer(@PathVariable int id, @RequestBody DealerEntity dealer) {
        dealer.setId(id);
        dealerService.saveDealer(dealer);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDealer(@PathVariable int id) {
        dealerService.deleteDealer(id);
        return ResponseEntity.noContent().build();
    }
}
