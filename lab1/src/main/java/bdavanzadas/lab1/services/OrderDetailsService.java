package bdavanzadas.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.OrderDetailsEntity;
import bdavanzadas.lab1.repositories.OrderDetailsRepository;

import java.util.List;


@Service
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;

    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
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






}
