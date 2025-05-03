package bdavanzadas.lab1.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsEntity {
    private int id;
    private int orderId;
    private String paymentMethod;
    private int totalProducts;
    private double price;

}
