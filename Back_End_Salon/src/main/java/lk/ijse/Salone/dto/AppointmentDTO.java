package lk.ijse.Salone.dto;


import lk.ijse.Salone.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentDTO {
    private int appointmentId;
    private SalonDTO serviceDTO;
    private StylistDTO stylistDTO;
    private UserDTO user;
    private Date appointmentDate;
    private String appointmentTime;
    private String appointmentStatus;
    private PaymentDTO paymentDTO;

}
