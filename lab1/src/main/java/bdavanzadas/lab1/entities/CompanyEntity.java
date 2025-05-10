package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyEntity {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String rut;
    private String type;
    private int deliveries;
    private int failedDeliveries;
    private int totalSales;

}
