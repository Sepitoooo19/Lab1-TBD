package bdavanzadas.lab1.repositories;

import java.util.List;
import java.util.Map;

import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.entities.ClientEntity;

public interface OrdersRepositoryInt {

    List<OrdersEntity> findAll();
    void save(OrdersEntity order);
    void update(OrdersEntity order);
    void delete(int id);
    OrdersEntity findById(int id);
    List<OrdersEntity> findByClientId(int clientId);
    List<OrdersEntity> findByDealerId(int dealerId);
    Map<String, Object> getTopSpender();

}
