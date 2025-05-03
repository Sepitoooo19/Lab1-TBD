package bdavanzadas.lab1.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedDeliveriesCompanyDTO {
    private String companyName;
    private int failedDeliveries;
}
