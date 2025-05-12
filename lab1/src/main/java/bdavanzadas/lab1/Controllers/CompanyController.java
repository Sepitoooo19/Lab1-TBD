package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.CompanyEntity;
import bdavanzadas.lab1.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/companies")
@CrossOrigin(origins = "*")
public class CompanyController {
    @Autowired
    private CompanyService service;

    @GetMapping
    public ResponseEntity<List<CompanyEntity>> getAllCompanies() {
        List<CompanyEntity> companies = service.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyEntity> getCompanyById(@PathVariable int id) {
        System.out.println("Solicitud recibida para obtener la empresa con ID: " + id);
        CompanyEntity company = service.findbyid(id);
        if (company == null) {
            System.out.println("Empresa no encontrada");
            return ResponseEntity.notFound().build();
        }
        System.out.println("Empresa encontrada: " + company.getName());
        return ResponseEntity.ok(company);
    }
    //1. **Endpoints básicos CRUD**
    @PostMapping("/crear")
    public void create(@RequestBody CompanyEntity c) {
        service.saveCompany(c);
    }

    @PostMapping("/update")
    public void update(@RequestBody CompanyEntity c) {
        service.updateCompany(c);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        service.deleteCompany(id);
    }
    //- Obtiene las compañías con más entregas fallidas
    @GetMapping("/failed-deliveries")
    public ResponseEntity<List<CompanyEntity>> getCompaniesWithMostFailedDeliveries() {
        List<CompanyEntity> companies = service.getCompaniesWithMostFailedDeliveries();
        return ResponseEntity.ok(companies);
    }
    //- Actualiza las métricas de todas las compañías
    @PostMapping("/update-metrics")
    public ResponseEntity<Void> updateCompanyMetrics() {
        service.updateCompanyMetrics();
        return ResponseEntity.ok().build();
    }
    //- Obtiene las compañías ordenadas por volumen de comida entregada
    @GetMapping("/delivered-food-volume")
    public ResponseEntity<List<Map<String, Object>>> getCompaniesByDeliveredFoodVolume() {
        List<Map<String, Object>> companies = service.getCompaniesByDeliveredFoodVolume();
        return ResponseEntity.ok(companies);
    }
}