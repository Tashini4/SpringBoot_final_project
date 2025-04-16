package lk.ijse.Salone.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    private int appointmentId;

    @ManyToOne
    @JoinColumn(name="service_id")
    private Salon service;

    @ManyToOne
    @JoinColumn(name="stylist_id")
    private Stylist stylist;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private Date appointmentDate;
    private String appointmentTime;

    private String appointmentStatus;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Payment payment;

}
