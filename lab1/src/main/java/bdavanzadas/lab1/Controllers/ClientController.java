package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "*") // Permite llamadas desde tu frontend Nuxt
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientEntity>> getAllClients() {
        List<ClientEntity> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable int id) {
        ClientEntity client = clientService.getClientById(id);
        if (client != null) {
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ClientEntity> createClient(@RequestBody ClientEntity client) {
        clientService.saveClient(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }
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
