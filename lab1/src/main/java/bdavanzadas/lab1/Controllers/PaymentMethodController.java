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

    // -----------------------------------------------------------------
    // ENDPOINTS CRUD
    // -----------------------------------------------------------------

    /** Obtiene todos los métodos de pago existentes. */
    @GetMapping("/obtenertodos")
    public List<PaymentMethodEntity> getAll() {
        return service.getallPaymentMethods();
    }

    /** Obtiene un método de pago por su ID. */
    @GetMapping("/obtenerbyid/{id}")
    public PaymentMethodEntity getById(@PathVariable int id) {
        return service.getPaymentMethodById(id);
    }

    /** Obtiene un método de pago por su tipo (e.g., 'TARJETA', 'EFECTIVO'). */
    @GetMapping("/obtenerbytype/{type}")
    public PaymentMethodEntity getByType(@PathVariable String type) {
        return service.getPaymentMethodByType(type);
    }

    /** Crea un nuevo método de pago. Devuelve 201 CREATED. */
    @PostMapping("/crear")
    public ResponseEntity<Void> create(@RequestBody PaymentMethodEntity p) {
        service.savePaymentMethod(p);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Actualiza un método de pago existente.
     * <p>Conviene usar {@code @PutMapping} semánticamente,
     * pero se mantiene {@code @PostMapping} según el código original.</p>
     */
    @PostMapping("/update")
    public void update(@RequestBody PaymentMethodEntity p) {
        service.updatePaymentMethod(p);
    }

    /**
     * Elimina un método de pago por ID.
     * <p>También podría ser un {@code @DeleteMapping} para seguir
     * las convenciones REST.</p>
     */
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        service.deletePaymentMethod(id);
    }

    // -----------------------------------------------------------------
    // CONSULTAS ESPECÍFICAS
    // -----------------------------------------------------------------

    /** Métodos de pago asociados a una empresa específica. */
    @GetMapping("/company/{companyId}")
    public List<PaymentMethodEntity> getByCompanyId(@PathVariable int companyId) {
        return service.getPaymentMethodsByCompanyId(companyId);
    }
}
