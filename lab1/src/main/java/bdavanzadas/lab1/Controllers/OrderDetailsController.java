package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.OrderDetailsEntity;
import bdavanzadas.lab1.services.OrderDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/order-details")
@CrossOrigin(origins = "*") // Permite llamadas desde tu frontend Nuxt
public class OrderDetailsController {

    private final OrderDetailsService orderDetailsService;

    public OrderDetailsController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailsEntity>> getAllOrderDetails() {
        return ResponseEntity.ok(orderDetailsService.getAllOrderDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsEntity> getOrderDetailById(@PathVariable int id) {
        OrderDetailsEntity orderDetail = orderDetailsService.getOrderDetailsById(id);
        if (orderDetail != null) {
            return ResponseEntity.ok(orderDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> addOrderDetail(@RequestBody OrderDetailsEntity orderDetail) {
        orderDetailsService.saveOrderDetails(orderDetail);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrderDetail(@PathVariable int id, @RequestBody OrderDetailsEntity orderDetail) {
        orderDetail.setId(id);
        orderDetailsService.saveOrderDetails(orderDetail);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable int id) {
        orderDetailsService.deleteOrderDetails(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailsEntity>> getOrderDetailsByOrderId(@PathVariable int orderId) {
        List<OrderDetailsEntity> orderDetails = orderDetailsService.getOrderDetailsByOrderId(orderId);
        if (orderDetails != null && !orderDetails.isEmpty()) {
            return ResponseEntity.ok(orderDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/ObtenerMediodepagoUrgente")
    public String obtenerMedioPagoUrgente(){
        return orderDetailsService.findPaymentmethodUrgentOrders();
    }
}
