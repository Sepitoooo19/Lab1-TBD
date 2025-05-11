package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.dtos.TopSpenderDTO;
import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.entities.ProductEntity;
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

    @GetMapping("/client/orders")
    public ResponseEntity<List<OrdersEntity>> getOrdersByClient() {
        try {
            List<OrdersEntity> orders = ordersService.getOrdersByClientId();
            return ResponseEntity.ok(orders); // Devuelve un array vacío si no hay pedidos
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Devuelve 403 si hay un error de validación
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Devuelve 500 para errores inesperados
        }
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<OrdersEntity>> getOrdersByClientId(@PathVariable int id) {
        List<OrdersEntity> orders = ordersService.getOrdersByClientId(id);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/top-spender")
    public ResponseEntity<TopSpenderDTO> getTopSpender() {
        TopSpenderDTO topSpender = ordersService.getTopSpender();
        return ResponseEntity.ok(topSpender);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestBody OrdersEntity order, // Cuerpo de la solicitud
            @RequestParam List<Integer> productIds // Parámetro de consulta
    ) {
        System.out.println("Orden recibida: " + order);
        System.out.println("IDs de productos recibidos: " + productIds);

        try {
            ordersService.createOrderWithProducts(order, productIds);
            return ResponseEntity.ok("Orden creada exitosamente");
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error completo en los logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la orden: " + e.getMessage());
        }
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

    //Endpoint para obtener los pedidos por el ID de la empresa

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<OrdersEntity>> getOrdersByCompanyId(@PathVariable int companyId) {
        List<OrdersEntity> orders = ordersService.findOrdersByCompanyId(companyId);
        if (orders != null && !orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/dealer/{id}")
    public ResponseEntity<List<OrdersEntity>> getOrdersByDealerId(@PathVariable int id) {
        List<OrdersEntity> orders = ordersService.getOrdersByDealerId(id);
        if (orders != null && !orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/dealer/{dealerId}/status")
    public ResponseEntity<Void> updateOrderStatusByDealerId(
            @PathVariable int id,
            @PathVariable int dealerId,
            @RequestBody Map<String, String> requestBody) {
        System.out.println("Actualizando estado de la orden...");
        System.out.println("ID de la orden: " + id);
        System.out.println("ID del dealer: " + dealerId);
        System.out.println("Nuevo estado: " + requestBody.get("status"));

        ordersService.updateOrderStatusByDealerId(id, dealerId, requestBody.get("status"));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/products")
    public ResponseEntity<List<ProductEntity>> getProductsByOrderId(@PathVariable int orderId) {
        try {
            List<ProductEntity> products = ordersService.findProductsByOrderId(orderId);
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build(); // Devuelve 204 si no hay productos
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devuelve 500 para errores inesperados
        }
    }

    //    getLastInsertedOrderId
    @GetMapping("/last-inserted")
    public ResponseEntity<Integer> getLastInsertedOrderId() {
        try {
            int lastInsertedOrderId = ordersService.getLastInsertedOrderId(); // Llama al servicio
            return ResponseEntity.ok(lastInsertedOrderId); // Devuelve el ID en la respuesta
        } catch (Exception e) {
            e.printStackTrace(); // Registra el error en los logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devuelve un error 500
        }
    }


    //getAddressOfLoggedClient
    @GetMapping("/address")
    public ResponseEntity<String> getAddressOfLoggedClient() {
        try {
            String address = ordersService.getAddressOfLoggedClient();
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/urgent")
    public ResponseEntity<String> markOrderAsUrgent(@PathVariable int id) {
        try {
            ordersService.markAsUrgent(id);
            return ResponseEntity.ok("Pedido marcado como URGENTE");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al marcar el pedido como URGENTE");
        }
    }








}
