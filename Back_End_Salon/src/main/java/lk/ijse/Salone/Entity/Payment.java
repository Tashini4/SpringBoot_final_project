package lk.ijse.Salone.Entity;

import jakarta.persistence.*;
import lk.ijse.Salone.dto.AppointmentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private double amount;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "payment_method")
    private String paymentMethod;


    @Column(name = "payment_status")
    private String paymentStatus;

}
