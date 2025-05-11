package bdavanzadas.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.OrderDetailsEntity;
import bdavanzadas.lab1.repositories.OrderDetailsRepository;

import java.util.List;

@Service
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrdersService ordersService; // Inyección del servicio de órdenes

    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository, OrdersService ordersService) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.ordersService = ordersService; // Inicialización del servicio de órdenes
    }

    @Transactional(readOnly = true)
    public List<OrderDetailsEntity> getAllOrderDetails() {
        return orderDetailsRepository.findAll();
    }

    @Transactional
    public void saveOrderDetails(OrderDetailsEntity orderDetails) {
        orderDetailsRepository.save(orderDetails);
    }

    @Transactional
    public void deleteOrderDetails(int id) {
        orderDetailsRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public OrderDetailsEntity getOrderDetailsById(int id) {
        return orderDetailsRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<OrderDetailsEntity> getOrderDetailsByOrderId(int orderId) {
        return orderDetailsRepository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public String findPaymentmethodUrgentOrders() {
        return orderDetailsRepository.findPaymentmethodUrgentOrders();
    }

    @Transactional
    public void createOrderDetailsForLastOrder(String paymentMethod, int totalProducts, double price) {
        // Obtener el ID del último pedido insertado
        int lastOrderId = ordersService.getLastInsertedOrderId();

        // Crear los detalles de la orden
        OrderDetailsEntity orderDetails = new OrderDetailsEntity();
        orderDetails.setOrderId(lastOrderId);
        orderDetails.setPaymentMethod(paymentMethod);
        orderDetails.setTotalProducts(totalProducts);
        orderDetails.setPrice(price);

        // Guardar los detalles en la base de datos
        orderDetailsRepository.save(orderDetails);
    }
}