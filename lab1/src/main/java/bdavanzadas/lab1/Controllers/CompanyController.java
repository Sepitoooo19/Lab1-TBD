package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.dtos.FailedDeliveriesCompanyDTO;
import bdavanzadas.lab1.entities.CompanyEntity;
import bdavanzadas.lab1.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companies")
@CrossOrigin(origins = "*")
public class CompanyController {
    @Autowired
    private CompanyService service;

    @GetMapping("/obtenertodos")
    public List<CompanyEntity> getAll() {return service.getAllCompanies();}
    @GetMapping("/obtenerporid/{id}")
    public CompanyEntity getById(@PathVariable int id) {return service.findbyid(id);}
    @PostMapping("/crear")
    public void create(@RequestBody CompanyEntity c) {service.saveCompany(c);}
    @PostMapping("/update")
    public void update(@RequestBody CompanyEntity c) {service.updateCompany(c);}
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {service.deleteCompany(id);}

    @GetMapping("/failed-deliveries")
    public ResponseEntity<List<FailedDeliveriesCompanyDTO>> getCompaniesWithMostFailedDeliveries() {
        List<FailedDeliveriesCompanyDTO> companies = service.getCompaniesWithMostFailedDeliveries();
        return ResponseEntity.ok(companies);
    }
}
