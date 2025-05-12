package bdavanzadas.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.repositories.ClientRepository;
import bdavanzadas.lab1.entities.UserEntity;

import java.util.List;


/**
 *
 * La clase ClientService representa el servicio de clientes en la aplicación.
 * Esta clase contiene métodos para guardar, actualizar, eliminar y buscar clientes en la base de datos.
 *
 * */
@Service
public class ClientService {


    /**
     * Repositorio de clientes.
     * Este repositorio se utiliza para interactuar con la base de datos de clientes.
     */
    private final ClientRepository clientRepository;


    /**
     * Constructor de la clase ClientService.
     * @param "clientRepository" El repositorio de clientes a utilizar.
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    /**
     * Metodo para obtener todos los clientes de la base de datos.
     * @return Una lista de clientes.
     *
     */
    @Transactional(readOnly = true)
    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }


    /**
     * Metodo para buscar un cliente por su id.
     * @param "id" El id del cliente a buscar.
     * @return El cliente encontrado.
     *
     */
    @Transactional(readOnly = true)
    public ClientEntity getClientById(int id) {
        return clientRepository.findById(id);
    }


    /**
     * Metodo para guardar un cliente en la base de datos.
     * @param "client" El cliente a guardar.
     * @return void
     */
    @Transactional
    public void saveClient(ClientEntity client) {
        clientRepository.save(client);
    }


    /**
     * Metodo para actualizar un cliente en la base de datos.
     * @param "client" El cliente a actualizar.
     * @return void
     *
     */
    @Transactional
    public void updateClient(ClientEntity client) {
        clientRepository.update(client);
    }


    /**
     * Metodo para eliminar un cliente de la base de datos.
     * @param "id" El id del cliente a eliminar.
     * @return void
     *
     */
    @Transactional
    public void deleteClient(int id) {
        clientRepository.delete(id);
    }


    /**
     * Metodo para buscar el nombre de un cliente por su id de usuario.
     * @param   "id" El id del cliente a buscar.
     * @return El nombre del cliente encontrado.
     *
     */
    @Transactional(readOnly = true)
    public String getNameByClientId(int id) {
        ClientEntity client = clientRepository.findById(id);
        return client != null ? client.getName() : null;
    }



}
