package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.CompanyEntity;
import bdavanzadas.lab1.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@CrossOrigin(origins = "*")
public class CompanyController {
    @Autowired
    private CompanyService service;

    @GetMapping
    public List<CompanyEntity> getAllCompanies() {
        List<CompanyEntity> companies = service.getAllCompanies();
        System.out.println("Compañías enviadas al cliente: " + companies);
        return companies;
    }
    @GetMapping("/obtenerporid/{id}")
    public CompanyEntity getById(@PathVariable int id) {return service.findbyid(id);}
    @PostMapping("/crear")
    public void create(@RequestBody CompanyEntity c) {service.saveCompany(c);}
    @PostMapping("/update")
    public void update(@RequestBody CompanyEntity c) {service.updateCompany(c);}
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {service.deleteCompany(id);}

    @GetMapping("/failed-deliveries")
    public ResponseEntity<List<CompanyEntity>> getCompaniesWithMostFailedDeliveries() {
        List<CompanyEntity> companies = service.getCompaniesWithMostFailedDeliveries();
        return ResponseEntity.ok(companies);
    }
}
