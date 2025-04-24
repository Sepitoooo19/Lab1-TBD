package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.OrderDetailsEntity;

import java.util.List;

public interface OrderDetailsRepositoryInt {

    List<OrderDetailsEntity> findAll();
    void save(OrderDetailsEntity orderDetails);
    void update(OrderDetailsEntity orderDetails);
    void delete(int id);
    OrderDetailsEntity findById(int id);
    List<OrderDetailsEntity> findByOrderId(int orderId);


}
