package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.ProductEntity;
import bdavanzadas.lab1.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    private ProductService service;
    @GetMapping("/obtenertodos")
    public List<ProductEntity> getAll() {return service.getAllProducts();}
    @GetMapping("/obtenerporstock")
    public List<ProductEntity> getStock() {return service.getProductsByStock();}
    @GetMapping("/obtenerporid/{id}")
    public ProductEntity getById(@PathVariable int id) {return service.getProductById(id);}
    @PostMapping("/crear")
    public void create(@RequestBody ProductEntity p) {service.saveProduct(p);}
    @PostMapping("/update")
    public void update(@RequestBody ProductEntity p) {service.updateProduct(p);}
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {service.deleteProduct(id);}
    @GetMapping("/obtenerporcategoria/{category}")
    public List<ProductEntity> getProductsByCategory(@PathVariable String category) {return service.getProductsByCategory(category);}
    @GetMapping("/categorias")
    public List<String> findallCategories(){return service.findallCategories();}
    @GetMapping("/top-by-category")
    public ResponseEntity<List<Map<String, Object>>> getTopProductsByCategoryForLastMonth() {
        List<Map<String, Object>> topProducts = service.getTopProductsByCategoryForLastMonth();
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/company/{id}")
    public List<ProductEntity> getProductsByCompanyId(@PathVariable int id) {
        return service.getProductsByCompanyId(id);
    }

    //getCompanyIdByProductId

    @GetMapping("/companyid/{id}")
    public int getCompanyIdByProductId(@PathVariable int id) {
        return service.getCompanyIdByProductId(id);
    }
}
