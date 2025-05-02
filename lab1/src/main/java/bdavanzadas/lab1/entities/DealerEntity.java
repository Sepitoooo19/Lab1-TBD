package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealerEntity {
    private int id;
    private String rut;
    private String name;
    private String phone;
    private String email;
    private String vehicle;
    private String plate;

    private int userId; // Relación con UserEntity
}