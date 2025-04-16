package lk.ijse.Salone.repo;

import lk.ijse.Salone.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment,Integer> {
}
