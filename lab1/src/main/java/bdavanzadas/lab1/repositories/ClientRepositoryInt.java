package bdavanzadas.lab1.repositories;



import java.util.List;

import bdavanzadas.lab1.entities.ClientEntity;


public interface ClientRepositoryInt {

    List<ClientEntity> findAll();
    void save(ClientEntity client);

    void update(ClientEntity client);
    void delete(int id);
    ClientEntity findById(int id);


}
