package lk.ijse.Salone.service.custom;

import lk.ijse.Salone.dto.AppointmentDTO;
import lk.ijse.Salone.dto.PaymentDTO;



public interface AppointmentService {
    int saveAppointment(AppointmentDTO appointmentDTO, PaymentDTO paymentDTO);
}
