package lk.ijse.Salone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Author: vishmee
 * Date: 4/15/25
 * Time: 1:29 AM
 * Description:
 */
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
