package bdavanzadas.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.repositories.OrdersRepository;

import java.util.List;

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
}
