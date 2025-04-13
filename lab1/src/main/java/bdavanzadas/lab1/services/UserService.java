package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public UserEntity getByUsername(String username) {
        return repo.findByUsername(username);
    }

    public void register(String username, String password, String role) {
        String encoded = encoder.encode(password);
        UserEntity user = new UserEntity(0, username, encoded, role);
        repo.save(user);
    }

    public boolean validateCredentials(String username, String password) {
        UserEntity user = repo.findByUsername(username);
        return encoder.matches(password, user.getPassword());
    }
}