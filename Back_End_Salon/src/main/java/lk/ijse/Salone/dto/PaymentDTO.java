package lk.ijse.Salone.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {
    private int paymentId;
    private AppointmentDTO appointmentDTO;
    private double amount;
    private String paymentDate;
    private String paymentMethod;
    private String paymentStatus;
}
