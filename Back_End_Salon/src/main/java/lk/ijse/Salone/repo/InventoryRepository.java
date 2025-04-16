package lk.ijse.Salone.repo;


import lk.ijse.Salone.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
}
