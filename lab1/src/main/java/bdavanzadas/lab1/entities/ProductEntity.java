package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {
    private int id;
    private String name;
    private int stock;
    private float price;
    private String proovedor;
}
