package lk.ijse.Salone.repo;

import lk.ijse.Salone.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepo extends JpaRepository<Appointment,Integer> {
}
