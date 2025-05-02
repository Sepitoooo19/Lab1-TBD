package bdavanzadas.lab1.repositories;

import java.util.List;
import bdavanzadas.lab1.entities.UserEntity;

public interface UserRepositoryInt {

    List<UserEntity> findAll();
    void save(UserEntity user);
    void update(UserEntity user);
    void delete(int id);
    UserEntity findById(int id);

}
