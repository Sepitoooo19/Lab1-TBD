package bdavanzadas.lab1.dtos;

import bdavanzadas.lab1.entities.OrdersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CrearOrdenDTO {
    OrdersEntity o;
    List<Integer> products;
}
