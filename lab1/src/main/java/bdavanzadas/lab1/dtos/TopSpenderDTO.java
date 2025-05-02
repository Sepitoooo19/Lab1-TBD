package bdavanzadas.lab1.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

// Esta clase es un DTO (Data Transfer Object) que representa a un cliente con el mayor gasto total en la tienda.
public class TopSpenderDTO {
    private int id;
    private String name;
    private String rut;
    private String email;
    private String phone;
    private String address;
    private double totalSpent;
}
