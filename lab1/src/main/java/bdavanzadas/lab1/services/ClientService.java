package bdavanzadas.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.repositories.ClientRepository;
import bdavanzadas.lab1.entities.UserEntity;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ClientEntity getClientById(int id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public void saveClient(ClientEntity client) {
        clientRepository.save(client);
    }

    @Transactional
    public void updateClient(ClientEntity client) {
        clientRepository.update(client);
    }

    @Transactional
    public void deleteClient(int id) {
        clientRepository.delete(id);
    }



}
