package lk.ijse.Salone.service.custom.IMPL;

import lk.ijse.Salone.Entity.Inventory;
import lk.ijse.Salone.dto.InventoryDTO;
import lk.ijse.Salone.repo.InventoryRepository;
import lk.ijse.Salone.service.custom.InventoryService;
import lk.ijse.Salone.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public int createInventoryItem(InventoryDTO inventoryDTO) {
        Inventory inventory = modelMapper.map(inventoryDTO, Inventory.class);
        inventoryRepository.save(inventory);
        return VarList.Created;
    }

    @Override
    public InventoryDTO getInventoryItemById(int inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));
        return modelMapper.map(inventory, InventoryDTO.class);
    }

    @Override
    public List<InventoryDTO> getAllInventoryItems() {
        List<Inventory> inventoryItems = inventoryRepository.findAll();
        return inventoryItems.stream()
                .map(inventory -> modelMapper.map(inventory, InventoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public int updateInventoryItem(InventoryDTO inventoryDTO) {
        try {
            if (inventoryRepository.existsById(inventoryDTO.getInventoryId())) {
                Inventory inventory = modelMapper.map(inventoryDTO, Inventory.class);
                inventoryRepository.save(inventory);
                return VarList.OK;
            } else {
                return VarList.Not_Found;
            }
        } catch (Exception e) {
            return VarList.Bad_Gateway;
        }
    }

    @Override
    public void deleteInventoryItem(int inventoryId) {
        inventoryRepository.deleteById(inventoryId);

    }
}
