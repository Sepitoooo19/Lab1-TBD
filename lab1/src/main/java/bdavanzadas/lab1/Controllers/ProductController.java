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

    // -----------------------------------------------------------------
    // CRUD BÁSICO
    // -----------------------------------------------------------------

    /** Devuelve todos los productos. */
    @GetMapping("/obtenertodos")
    public List<ProductEntity> getAll() {
        return service.getAllProducts();
    }

    /** Devuelve productos con stock disponible (&gt;0). */
    @GetMapping("/obtenerporstock")
    public List<ProductEntity> getStock() {
        return service.getProductsByStock();
    }

    /** Obtiene un producto por su ID. */
    @GetMapping("/obtenerporid/{id}")
    public ProductEntity getById(@PathVariable int id) {
        return service.getProductById(id);
    }

    /** Crea un nuevo producto. Devuelve 200 OK sin body (podrías usar 201 CREATED). */
    @PostMapping("/crear")
    public void create(@RequestBody ProductEntity p) {
        service.saveProduct(p);
    }

    /**
     * Actualiza un producto existente.
     * <p>Se mantiene como POST para respetar tu implementación,
     * aunque REST aconsejaría {@code PUT /{id}}.</p>
     */
    @PostMapping("/update")
    public void update(@RequestBody ProductEntity p) {
        service.updateProduct(p);
    }

    /** Elimina un producto. (Igual comentario: lo típico sería {@code DELETE /{id}}). */
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        service.deleteProduct(id);
    }

    // -----------------------------------------------------------------
    // CONSULTAS ESPECÍFICAS
    // -----------------------------------------------------------------

    /** Productos por categoría (string). */
    @GetMapping("/obtenerporcategoria/{category}")
    public List<ProductEntity> getProductsByCategory(@PathVariable String category) {
        return service.getProductsByCategory(category);
    }

    /** Lista de todas las categorías distintas disponibles. */
    @GetMapping("/categorias")
    public List<String> findallCategories() {
        return service.findallCategories();
    }

    /**
     * Productos más vendidos/solicitados por categoría en el último mes.
     *
     * @return Lista de mapas con la siguiente estructura, por ejemplo:<br>
     * {@code [{ "category": "Electrónica", "productId": 1, "total": 50 }, ...]}
     */
    @GetMapping("/top-by-category")
    public ResponseEntity<List<Map<String, Object>>> getTopProductsByCategoryForLastMonth() {
        List<Map<String, Object>> topProducts = service.getTopProductsByCategoryForLastMonth();
        return ResponseEntity.ok(topProducts);
    }

    /** Productos pertenecientes a una empresa (company) específica. */
    @GetMapping("/company/{id}")
    public List<ProductEntity> getProductsByCompanyId(@PathVariable int id) {
        return service.getProductsByCompanyId(id);
    }

    // -----------------------------------------------------------------
    // UTILIDAD
    // -----------------------------------------------------------------

    /** Devuelve el companyId (empresa propietaria) de un producto. */
    @GetMapping("/companyid/{id}")
    public int getCompanyIdByProductId(@PathVariable int id) {
        return service.getCompanyIdByProductId(id);
    }
}
