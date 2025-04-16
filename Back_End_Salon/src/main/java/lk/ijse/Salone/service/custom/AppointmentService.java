package lk.ijse.Salone.service.custom;

import lk.ijse.Salone.dto.AppointmentDTO;
import lk.ijse.Salone.dto.PaymentDTO;

/**
 * Author: vishmee
 * Date: 4/15/25
 * Time: 2:11 AM
 * Description:
 */

public interface AppointmentService {
    int saveAppointment(AppointmentDTO appointmentDTO, PaymentDTO paymentDTO);
}
