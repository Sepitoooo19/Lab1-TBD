package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.entities.DealerEntity;
import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.repositories.ClientRepository;
import bdavanzadas.lab1.repositories.DealerRepository;
import bdavanzadas.lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DealerRepository dealerRepository;



    public void registerAdmin(String username, String password) {
        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    public void registerClient(String username, String password, String name, String rut, String email, String phone, String address) {
        // Verificar si el username ya existe
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso: " + username);
        }

        // Crear el usuario
        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("CLIENT");
        userRepository.save(user);

        // Verificar que el ID del usuario fue generado
        if (user.getId() == 0) {
            throw new IllegalStateException("No se generó un ID para el usuario");
        }

        // Crear el cliente asociado al usuario
        ClientEntity client = new ClientEntity();
        client.setName(name);
        client.setRut(rut);
        client.setEmail(email);
        client.setPhone(phone);
        client.setAddress(address);
        client.setUserId(user.getId()); // Asignar el ID del usuario al cliente
        clientRepository.save(client); // Guardar el cliente
    }


    public void registerDealer(String username, String password, String name, String rut, String email, String phone, String vehicle, String plate) {
        // Verificar si el username ya existe
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso: " + username);
        }

        // Crear y guardar el usuario
        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("DEALER");
        userRepository.save(user);

        // Verificar que el ID del usuario fue generado correctamente
        if (user.getId() == 0) {
            throw new IllegalStateException("No se generó un ID para el usuario");
        }

        // Crear y guardar el dealer
        DealerEntity dealer = new DealerEntity();
        dealer.setName(name);
        dealer.setRut(rut);
        dealer.setEmail(email);
        dealer.setPhone(phone);
        dealer.setVehicle(vehicle);
        dealer.setPlate(plate);
        dealer.setUserId(user.getId()); // Relación con el usuario

        dealerRepository.save(dealer);
    }

    public UserEntity validateCredentials(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}