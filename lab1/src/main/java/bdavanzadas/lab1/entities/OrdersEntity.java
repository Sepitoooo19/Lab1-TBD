package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersEntity {
    private int id;
    private Date orderDate;
    private Date deliveryDate;
    private String status;
    private int clientId;
    private int dealerId;
    private double totalPrice;
}
