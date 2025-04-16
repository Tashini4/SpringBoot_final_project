package lk.ijse.Salone.service.custom;

import lk.ijse.Salone.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {
    int createInventoryItem(InventoryDTO inventoryDTO);
    InventoryDTO getInventoryItemById(int inventoryId);
    List<InventoryDTO> getAllInventoryItems();
    int updateInventoryItem(InventoryDTO inventoryDTO);
    void deleteInventoryItem(int inventoryId);


}
