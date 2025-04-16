package lk.ijse.Salone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.processing.SQL;

import java.sql.Date;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryDTO {
    private int inventoryId;
    private String itemName;
    private int quantity;
    private String category;
    private Date lastRestocked;
}
