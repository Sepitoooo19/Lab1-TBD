package bdavanzadas.lab1.repositories;

import java.util.List;


import bdavanzadas.lab1.dtos.TopSpenderDTO;
import bdavanzadas.lab1.entities.OrdersEntity;


public interface OrdersRepositoryInt {

    List<OrdersEntity> findAll();
    void save(OrdersEntity order);
    void update(OrdersEntity order);
    void delete(int id);
    OrdersEntity findById(int id);
    List<OrdersEntity> findByClientId(int clientId);
    List<OrdersEntity> findByDealerId(int dealerId);
    TopSpenderDTO getTopSpender();

}
