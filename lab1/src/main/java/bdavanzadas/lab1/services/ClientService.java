package bdavanzadas.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.repositories.ClientRepository;

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

    @Transactional
    public void addClient(ClientEntity client) {
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

    @Transactional(readOnly = true)
    public ClientEntity getClientById(int id) {
        return clientRepository.findById(id);
    }
}
