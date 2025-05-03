package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {
    private int id;
    private String name;
    private String rut;
    private String email;
    private String phone;
    private String address;


    private int userId; // Relación con UserEntity
}