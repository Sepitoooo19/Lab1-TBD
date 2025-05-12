package bdavanzadas.lab1.services;

import bdavanzadas.lab1.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.DealerEntity;
import bdavanzadas.lab1.repositories.DealerRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DealerService {
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<DealerEntity> getAllDealers() {
        return dealerRepository.findAll();
    }
    @Transactional(readOnly = true)
    public DealerEntity getDealerById(int id) {
        return dealerRepository.findById(id);
    }
    @Transactional
    public void saveDealer(DealerEntity dealer) {
        dealerRepository.save(dealer);
    }
    @Transactional
    public void updateDealer(DealerEntity dealer) {
        dealerRepository.update(dealer);
    }
    @Transactional
    public void deleteDealer(int id) {
        dealerRepository.delete(id);
    }

    public String getDealerNameById(Integer dealerId) {
        if (dealerId == null) {
            return "Sin asignar"; // Si el dealerId es null, devuelve "Sin asignar"
        }
        return dealerRepository.findDealerNameById(dealerId);
    }

    //RF 05 TOP 3 MEJORES REPARTIDORES
    public List<Map<String, Object>> getTopPerformingDealers() {
        return dealerRepository.getTopPerformingDealers();
    }

    //RF 04: TIEMPO PROMEDIO DE ESPERA
    public List<Map<String, Object>> getAverageDeliveryTimeByDealer() {
        return dealerRepository.getAverageDeliveryTimeByDealer();
    }

    @Transactional(readOnly = true)
    public Double getAverageDeliveryTimeByAuthenticatedDealer() {
        // Obtener el ID del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();
        return dealerRepository.getAverageDeliveryTimeByAuthenticatedDealer(userId);
    }
    @Transactional(readOnly = true)
    public Integer getDeliveryCountByAuthenticatedDealer() {
        // Obtener el ID del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();
        return dealerRepository.getDeliveryCountByAuthenticatedDealer(userId);
    }

}



