package lk.ijse.Salone.service.custom.IMPL;

import jakarta.transaction.Transactional;
import lk.ijse.Salone.Entity.Appointment;
import lk.ijse.Salone.Entity.Payment;
import lk.ijse.Salone.controller.MailController;
import lk.ijse.Salone.dto.AppointmentDTO;
import lk.ijse.Salone.dto.PaymentDTO;
import lk.ijse.Salone.repo.AppointmentRepo;
import lk.ijse.Salone.repo.PaymentRepo;
import lk.ijse.Salone.service.custom.AppointmentService;
import lk.ijse.Salone.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppointmentRepo appointmentRepo;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private MailController mailController;

    @Override
    public int saveAppointment(AppointmentDTO appointmentDTO, PaymentDTO paymentDTO){
        try{
            Appointment appointment = modelMapper.map(appointmentDTO,Appointment.class);
            appointmentRepo.save(appointment);
            if(appointment != null){
                Payment payment = modelMapper.map(paymentDTO,Payment.class);
                payment.setAppointment(appointment);
                paymentRepo.save(payment);
                mailController.sendAppointmentReceipt(appointment);
                return VarList.Created;

            }
        } catch (Exception e) {
            return VarList.Not_Found;
        }

        return 0;
    }
}