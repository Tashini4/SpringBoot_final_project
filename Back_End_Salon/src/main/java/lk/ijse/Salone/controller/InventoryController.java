package lk.ijse.Salone.controller;


import lk.ijse.Salone.dto.InventoryDTO;
import lk.ijse.Salone.dto.ResponseDTO;
import lk.ijse.Salone.service.custom.InventoryService;
import lk.ijse.Salone.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/items")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/saveItems")
    public ResponseEntity<ResponseDTO> createInventoryItem(@RequestBody InventoryDTO inventoryDTO) {
       try{
           System.out.println(inventoryDTO.getInventoryId());
           System.out.println(inventoryDTO.getItemName());
           System.out.println(inventoryDTO.getQuantity());
           System.out.println(inventoryDTO.getCategory());
           System.out.println(inventoryDTO.getLastRestocked());

           LocalDate localDate = LocalDate.now();
           Date JoinDate = Date.valueOf(localDate);
           inventoryDTO.setLastRestocked(JoinDate);
           int res = inventoryService.createInventoryItem(inventoryDTO);
            switch (res) {
                case VarList.Created ->{
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", inventoryDTO));

                }default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }

            }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryDTO> getInventoryItemById(@PathVariable int inventoryId) {
        InventoryDTO inventoryDTO = inventoryService.getInventoryItemById(inventoryId);
        return ResponseEntity.ok(inventoryDTO);
    }

    @GetMapping("/getItems")
    public ResponseEntity<List<InventoryDTO>> getAllInventoryItems() {
        List<InventoryDTO> inventoryItems = inventoryService.getAllInventoryItems();
        return ResponseEntity.ok(inventoryItems);
    }

    @PutMapping("/updateItems")
    public ResponseEntity<ResponseDTO> updateInventoryItem(@RequestBody InventoryDTO inventoryDTO) {
        System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        System.out.println(inventoryDTO.getInventoryId());
        System.out.println(inventoryDTO.getItemName());
        System.out.println(inventoryDTO.getQuantity());
        System.out.println(inventoryDTO.getCategory());
        System.out.println(inventoryDTO.getLastRestocked());


          try{
              LocalDate localDate = LocalDate.now();
              Date JoinDate = Date.valueOf(localDate);
              inventoryDTO.setLastRestocked(Date.valueOf(JoinDate.toLocalDate()));
              int res = inventoryService.updateInventoryItem(inventoryDTO);
              switch(res){
                  case VarList.OK -> {
                      return ResponseEntity.status(HttpStatus.OK)
                              .body(new ResponseDTO(VarList.OK,"Success",inventoryDTO));
                  }
                  case VarList.Not_Found -> {
                      return ResponseEntity.status(HttpStatus.NOT_FOUND)
                              .body(new ResponseDTO(VarList.Not_Found,"Item not found",null));
                  }
                  default -> {
                      return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                              .body(new ResponseDTO(VarList.Bad_Gateway,"Error",null));
                  }
              }
          }catch (Exception e) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body(new ResponseDTO(VarList.Internal_Server_Error,"Error" +e.getMessage(),null));
          }
    }

    @DeleteMapping("/deleteItems/{inventoryId}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable int inventoryId) {
        inventoryService.deleteInventoryItem(inventoryId);
        return ResponseEntity.noContent().build();
    }
}

