package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 * La clase ClientController maneja las solicitudes relacionadas con los clientes.
 * Esta clase contiene métodos para obtener, crear, actualizar y eliminar clientes en la base de datos.
 *
 * */
@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "*") // Permite llamadas desde tu frontend Nuxt
public class ClientController {


    /**
     *
     * Servicio de clientes.
     * Este servicio se utiliza para interactuar con la base de datos de clientes.
     *
     *
     * */
    private final ClientService clientService;


    /**
     *
     * Constructor de la clase ClientController.
     * @param "clientService" El servicio de clientes a utilizar.
     *
     * */
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     *
     * Endpoint para obtener todos los clientes.
     * Este endpoint devuelve una lista de todos los clientes en la base de datos.
     *
     * */

    @GetMapping
    public ResponseEntity<List<ClientEntity>> getAllClients() {
        List<ClientEntity> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    /**
     *
     * Endpoint para obtener un cliente por su ID.
     * Este endpoint devuelve un cliente específico basado en su ID.
     *
     * */

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable int id) {
        ClientEntity client = clientService.getClientById(id);
        if (client != null) {
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     *
     * Endpoint para crear un nuevo cliente.
     * Este endpoint guarda un nuevo cliente en la base de datos.
     *
     * */
    @PostMapping
    public ResponseEntity<ClientEntity> createClient(@RequestBody ClientEntity client) {
        clientService.saveClient(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }


    /**
     *
     * Endpoint para actualizar un cliente existente.
     * Este endpoint actualiza un cliente específico basado en su ID.
     *
     * */
    @PutMapping("/{id}")
    public ResponseEntity<ClientEntity> updateClient(@PathVariable int id, @RequestBody ClientEntity client) {
        ClientEntity existingClient = clientService.getClientById(id);
        if (existingClient != null) {
            client.setId(id);
            clientService.updateClient(client);
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     *
     * Endpoint para eliminar un cliente.
     * Este endpoint elimina un cliente específico basado en su ID.
     *
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable int id) {
        ClientEntity existingClient = clientService.getClientById(id);
        if (existingClient != null) {
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     *
     * Endpoint para obtener el nombre de un cliente por su ID.
     * Este endpoint devuelve el nombre de un cliente específico basado en su ID.
     *
     * */
    @GetMapping("/name/{id}")
    public ResponseEntity<String> getClientNameById(@PathVariable int id) {
        String clientName = clientService.getNameByClientId(id);
        if (clientName != null) {
            return new ResponseEntity<>(clientName, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
