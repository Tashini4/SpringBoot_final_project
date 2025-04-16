package lk.ijse.Salone.controller;

import lk.ijse.Salone.dto.ResponseDTO;
import lk.ijse.Salone.dto.SalonDTO;
import lk.ijse.Salone.dto.StylistDTO;
import lk.ijse.Salone.service.custom.StylistService;
import lk.ijse.Salone.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static lk.ijse.Salone.util.VarList.OK;

@RestController
@RequestMapping(value = "/api/v1/stylists")
@CrossOrigin
public class StylistController {
    @Autowired
    private StylistService stylistService;

    @PostMapping("/saveStylist")
    public ResponseEntity<ResponseDTO> saveService(@RequestPart("styleDTO") StylistDTO stylistDTO,
                                                   @RequestPart("file") MultipartFile file){
        System.out.println("ooooooooooooooooooooooooo");
        System.out.println(stylistDTO.getFullName());
        System.out.println(stylistDTO.getEmail());
        System.out.println(stylistDTO.getPhoneNumber());
        System.out.println(stylistDTO.getSpecialization());
        System.out.println(stylistDTO.getAvailability());
        try {
            if (file!= null && !file.isEmpty()){
                Path uploadDir = Paths.get(System.getProperty("user.dir") + "/uploads/");
                if (!Files.exists(uploadDir)){
                    Files.createDirectories(uploadDir);
                }
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);

            }

            int result = stylistService.saveStylist(stylistDTO,file);
            switch (result) {
                case VarList.Created ->{
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", stylistDTO));

                }default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
    }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/updateStylist")
    public ResponseEntity<ResponseDTO> updateStylist(@RequestBody StylistDTO stylistDTO){
        try{
            int result = stylistService.updateStylist(stylistDTO);
            switch (result){
                case OK -> {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(OK,"Success Stylist updated!",stylistDTO));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found,"Stylist not found",null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway,"Error",null));
                }
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,"Error" + e.getMessage(),null));
        }
    }
    @GetMapping("/getStylist")
    public ResponseEntity<ResponseDTO> getStylist(){
        List<StylistDTO> stylistList = stylistService.getStylist();
        return ResponseEntity.ok(
                new ResponseDTO(OK, "List",stylistList)
        );
    }
    @DeleteMapping("/deleteStylist/{stylistId}")
    public ResponseEntity<Void> deleteStylist(@PathVariable int stylistId){
        stylistService.deleteStylist(stylistId);
        return ResponseEntity.noContent().build();
    }
}



