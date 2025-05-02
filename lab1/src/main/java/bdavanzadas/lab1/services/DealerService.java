package bdavanzadas.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.DealerEntity;
import bdavanzadas.lab1.repositories.DealerRepository;



import java.util.List;

@Service
public class DealerService {

    private final DealerRepository dealerRepository;

    public DealerService(DealerRepository dealerRepository) {
        this.dealerRepository = dealerRepository;
    }

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


}
