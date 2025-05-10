package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.dtos.CrearOrdenDTO;
import bdavanzadas.lab1.dtos.TopSpenderDTO;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.services.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*") // Permite llamadas desde tu frontend Nuxt
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    public ResponseEntity<List<OrdersEntity>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdersEntity> getOrderById(@PathVariable int id) {
        OrdersEntity order = ordersService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<Void> addOrder(@RequestBody OrdersEntity order) {
        ordersService.addOrder(order);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrder(@PathVariable int id, @RequestBody OrdersEntity order) {
        order.setId(id);
        ordersService.updateOrder(order);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        ordersService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrdersEntity>> getOrdersByClientId(@PathVariable int clientId) {
        List<OrdersEntity> orders = ordersService.getOrdersByClientId(clientId);
        if (orders != null && !orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/top-spender")
    public ResponseEntity<TopSpenderDTO> getTopSpender() {
        TopSpenderDTO topSpender = ordersService.getTopSpender();
        return ResponseEntity.ok(topSpender);
    }

    @PostMapping("/CrearConProductos")
    public ResponseEntity<Void> addOrderWithProducts(@RequestBody CrearOrdenDTO or) {
        OrdersEntity order=or.getO();
        List<Integer> product=or.getProducts();
        ordersService.createOrderWithProducts(order,product);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/ObtenerPromedioEntregas")
    public List<List<Object>> obtenerPromedioTiempoEntregaPorRepartidor(){
        return  ordersService.obtenerPromedioTiempoEntregaPorRepartidor();
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<String> marcarComoEntregado(@PathVariable int id) {
        ordersService.markAsDelivered(id);
        return ResponseEntity.ok("Pedido marcado como entregado");


    }

    // Endpoint para obtener pedidos fallidos por ID de la empresa
    @GetMapping("/failed/company/{companyId}")
    public ResponseEntity<List<OrdersEntity>> getFailedOrdersByCompanyId(@PathVariable int companyId) {
        List<OrdersEntity> orders = ordersService.findFailedOrdersByCompanyId(companyId);
        return ResponseEntity.ok(orders);
    }

    // Endpoint para obtener pedidos entregados por ID de la empresa
    @GetMapping("/delivered/company/{companyId}")
    public ResponseEntity<List<OrdersEntity>> getDeliveredOrdersByCompanyId(@PathVariable int companyId) {
        List<OrdersEntity> orders = ordersService.findDeliveredOrdersByCompanyId(companyId);
        return ResponseEntity.ok(orders);
    }
}
