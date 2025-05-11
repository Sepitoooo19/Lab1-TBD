package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.PaymentMethodEntity;
import bdavanzadas.lab1.services.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paymentmethod")
@CrossOrigin(origins = "*")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService service;

    @GetMapping("/obtenertodos")
    public List<PaymentMethodEntity> getAll() {
        return service.getallPaymentMethods();
    }


    @GetMapping("/obtenerbyid/{id}")
    public PaymentMethodEntity getByName(@PathVariable int id) {
        return service.getPaymentMethodById(id);
    }

    @GetMapping("/obtenerbytype/{type}")
    public PaymentMethodEntity getByType(@PathVariable String type) {
        return service.getPaymentMethodByType(type);
    }

    @PostMapping("/crear")
    public ResponseEntity<Void> create(@RequestBody PaymentMethodEntity p) {
        service.savePaymentMethod(p);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public void update(@RequestBody PaymentMethodEntity p) {
        service.updatePaymentMethod(p);
    }
    
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        service.deletePaymentMethod(id);
    }

    @GetMapping("/company/{companyId}")
    public List<PaymentMethodEntity> getByCompanyId(@PathVariable int companyId) {
        return service.getPaymentMethodsByCompanyId(companyId);
    }
}
