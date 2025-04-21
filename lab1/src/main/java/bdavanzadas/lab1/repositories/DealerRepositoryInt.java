package bdavanzadas.lab1.repositories;



import java.util.List;
import bdavanzadas.lab1.entities.DealerEntity;


public interface DealerRepositoryInt {

    List<DealerEntity> findAll();
    void save(DealerEntity dealer);

    void update(DealerEntity dealer);
    void delete(int id);
    DealerEntity findById(int id);


}
