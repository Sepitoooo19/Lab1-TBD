package bdavanzadas.lab1.services;

import bdavanzadas.lab1.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.DealerEntity;
import bdavanzadas.lab1.repositories.DealerRepository;


import java.util.ArrayList;
import java.util.List;

@Service
public class DealerService {
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private OrdersRepository ordersRepository;

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

    //RF 05 TOP 3 MEJORES REPARTIDORES
    public List<List<Object>> obtenerTop3Repartidores(){
        List<DealerEntity> dealers= dealerRepository.findAll();
        List<List<Object>> result=new ArrayList<>();
        float tiempoprom=0;
        float ratingprom=0;
        float totalprom=0;
        for (DealerEntity dealer : dealers) {
            int id=dealer.getId();
            tiempoprom=ordersRepository.obtenerTiempoPromedioHoras(id);
            ratingprom=dealerRepository.puntuacionProm(id);
            totalprom=(tiempoprom+ratingprom)/2;
            List<Object> dealerResult=new ArrayList<>();
            dealerResult.add(id);
            dealerResult.add(tiempoprom);
            result.add(dealerResult);
        }
        // Ordenar los resultados según el rendimiento (totalprom) en orden descendente
        result.sort((a, b) -> Float.compare((float) b.get(1), (float) a.get(1)));

        // Retornar solo los 3 mejores repartidores (o menos si hay menos)
        return result.size() > 3 ? result.subList(0, 3) : result;
    }


}
