package bdavanzadas.lab1.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderNameAddressDTO {
    private int id;
    private Date orderDate;
    private Date deliveryDate;
    private String status;
    private double totalPrice;
    private int clientId;
    private String nameClient; // Nombre del cliente
    private String address;
}
