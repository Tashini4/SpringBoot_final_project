package lk.ijse.Salone.controller;

import lk.ijse.Salone.dto.InventoryDTO;
import lk.ijse.Salone.dto.ResponseDTO;
import lk.ijse.Salone.dto.SalonDTO;
import lk.ijse.Salone.service.custom.SalonService;
import lk.ijse.Salone.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static lk.ijse.Salone.util.VarList.OK;

@RestController
@RequestMapping(value = "/api/v1/service")
@CrossOrigin
public class SalonServiceController {
    @Autowired
    private SalonService salonService;

    @PostMapping(value = "/saveService", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> saveService(@RequestPart("salonDTO") SalonDTO salonDTO,
                                                   @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                // Create uploads directory if it doesn't exist
                Path uploadDir = Paths.get(System.getProperty("user.dir") + "/uploads/");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                // Save file
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            int res = salonService.saveService(salonDTO, file);
            switch (res) {
                case VarList.Created -> {
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", salonDTO));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error saving service: " + e.getMessage(), null));
        }
    }
    @PutMapping(value = "/updateService", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateService(@RequestBody SalonDTO salonDTO) {
        try {
            int res = salonService.updateService(salonDTO);
            switch (res) {
                case VarList.OK -> {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.OK, "Success", salonDTO));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Service not found", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/deleteItems/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable int serviceId) {
        salonService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{serviceId}")
    public ResponseEntity<SalonDTO> getServiceById(@PathVariable int serviceId) {
        SalonDTO salonDTO = salonService.getServiceById(serviceId);
        return ResponseEntity.ok(salonDTO);
    }

    @GetMapping("/getService")
    public ResponseEntity<ResponseDTO> getService() {
        try {
            List<SalonDTO> salons = salonService.getAllService();
            return ResponseEntity.ok(
                    new ResponseDTO(OK, "Success", salons)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error retrieving services", null));
        }
    }
}
