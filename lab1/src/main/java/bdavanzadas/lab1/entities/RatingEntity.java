package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingEntity {
    private int id;
    private int rating;
    private String comment;
    private Date date;
    private int clientId;
    private int dealerId;
    private int orderId;
}
