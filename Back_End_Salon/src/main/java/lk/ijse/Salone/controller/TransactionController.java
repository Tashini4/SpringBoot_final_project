package lk.ijse.Salone.controller;

import lk.ijse.Salone.dto.*;
import lk.ijse.Salone.service.custom.AppointmentService;
import lk.ijse.Salone.service.custom.SalonService;
import lk.ijse.Salone.service.custom.StylistService;
import lk.ijse.Salone.service.custom.UserService;
import lk.ijse.Salone.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


@RestController
@RequestMapping(value = "/api/v1/transaction")
public class TransactionController {
    private final UserService userService;

    @Autowired
    private SalonService salonService;
    @Autowired
    private StylistService stylistService;
    @Autowired
    private AppointmentService appointmentService;

    public TransactionController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/saveTransaction")
    public ResponseEntity<ResponseDTO> updateService(@RequestBody TransactionDTO transactionDTO) {
        System.out.println("transaction,...............................................................");
    try {
        int serviceID = transactionDTO.getServiceId();
        int stylistID = transactionDTO.getStylistId();
        String userEmail = transactionDTO.getUserEmail();
        Date appointmentDate = transactionDTO.getAppointmentDate();
        String appointmentTime = transactionDTO.getAppointmentTime();
        LocalDate currentDate = LocalDate.now();


        SalonDTO salonDTO = salonService.getServiceById(serviceID);
        StylistDTO stylistDTO = stylistService.getStylistById(stylistID);
        UserDTO userDTO = userService.searchUser(userEmail);
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setAppointmentDate(appointmentDate);
        appointmentDTO.setServiceDTO(salonDTO);
        appointmentDTO.setStylistDTO(stylistDTO);
        appointmentDTO.setAppointmentStatus("conformed");
        appointmentDTO.setUser(userDTO);
        appointmentDTO.setAppointmentTime(appointmentTime);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentDate(String.valueOf(currentDate));
        paymentDTO.setPaymentMethod("bank Transfer");
        paymentDTO.setPaymentStatus("conformed");
        paymentDTO.setAmount(salonDTO.getServicePrice());

        int res = appointmentService.saveAppointment(appointmentDTO,paymentDTO);

        switch (res){
            case VarList.Created -> {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created,"Success",transactionDTO));
            }default -> {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ResponseDTO(VarList.Bad_Gateway,"Error",null));
            }
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }
    }
