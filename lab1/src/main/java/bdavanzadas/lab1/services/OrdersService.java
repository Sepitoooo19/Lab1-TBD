package bdavanzadas.lab1.services;

import bdavanzadas.lab1.dtos.TopSpenderDTO;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.entities.ProductEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.repositories.OrdersRepository;

import java.util.List;
import java.util.Map;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;

    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Transactional(readOnly = true)
    public List<OrdersEntity> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Transactional
    public void addOrder(OrdersEntity order) {
        ordersRepository.save(order);
    }

    @Transactional
    public void updateOrder(OrdersEntity order) {
        ordersRepository.update(order);
    }

    @Transactional
    public void deleteOrder(int id) {
        ordersRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public OrdersEntity getOrderById(int id) {
        return ordersRepository.findById(id);
    }

    // en el repository se hace un query para obtener todos los pedidos de un cliente
    @Transactional(readOnly = true)
    public List<OrdersEntity> getOrdersByClientId(int clientId) {
        return ordersRepository.findByClientId(clientId);
    }

    // en el repository se hace un query para obtener todos los pedidos de un dealer
    @Transactional(readOnly = true)
    public List<OrdersEntity> getOrdersByDealerId(int dealerId) {
        return ordersRepository.findByDealerId(dealerId);
    }

    // en el repository se hace un query para obtener el cliente que mas gasto
    @Transactional(readOnly = true)
    public TopSpenderDTO getTopSpender() {
        return ordersRepository.getTopSpender();
    }

    // Método para crear una orden y asociar productos
    @Transactional
    public void createOrderWithProducts(OrdersEntity order, List<Integer> productIds) {
        // Guardar la orden (sin obtener el ID aquí, ya que no lo necesitas en este método)
        ordersRepository.save(order);

        // Recuperar el ID de la orden recién insertada
        int orderId = ordersRepository.getLastInsertedOrderId();

        // Guardar la relación entre la orden y los productos
        ordersRepository.saveOrderProducts(orderId, productIds);
    }

}
