package lk.ijse.Salone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {
    private int serviceId;
    private int stylistId;
    private String userEmail;
    private Date appointmentDate;
    private String appointmentTime;
}
