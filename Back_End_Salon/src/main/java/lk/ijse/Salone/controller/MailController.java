package lk.ijse.Salone.controller;

import lk.ijse.Salone.Entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class MailController {
    @Autowired
    private JavaMailSender javaMailSender;

    public String sendAppointmentReceipt(Appointment appointment) {
        try {
            int appointmentID = appointment.getAppointmentId();
            String serviceName = appointment.getService().getServiceName();
            double servicePrice = appointment.getService().getServicePrice();
            int serviceDuration = appointment.getService().getDuration();
            String userEmail = appointment.getUser().getEmail();
            String userName = appointment.getUser().getName();
            String salonArtistName = appointment.getStylist().getFullName();
            String salonArtistContact = appointment.getStylist().getPhoneNumber();
            Date appointmentDate = (Date) appointment.getAppointmentDate();
            String time = appointment.getAppointmentTime();

            String formattedDate = new SimpleDateFormat("EEEE, MMMM dd, yyyy").format(appointmentDate);

            String subject = "Your Salon Appointment Confirmation #" + appointmentID;

            String message = String.format(
                    "Dear %s,\n\n" +
                            "Thank you for booking with us! Here are your appointment details:\n\n" +
                            "=============================================\n" +
                            "       SALON APPOINTMENT RECEIPT\n" +
                            "=============================================\n\n" +
                            "Appointment ID: %d\n" +
                            "Date: %s\n" +
                            "Time: %s\n\n" +
                            "Service Details:\n" +
                            "----------------\n" +
                            "Service: %s\n" +
                            "Duration: %d minutes\n" +
                            "Price: LKR %.2f\n\n" +
                            "Artist: %s\n" +
                            "Artist Contact: %s\n\n" +
                            "=============================================\n\n" +
                            "We look forward to serving you!\n\n" +
                            "Please arrive 10 minutes before your scheduled time.\n" +
                            "Cancellations require 24 hours notice.\n\n" +
                            "Best regards,\n" +
                            "Salone Team",
                    userName, appointmentID, formattedDate, time,
                    serviceName, serviceDuration, servicePrice,
                    salonArtistName, salonArtistContact);

            // Create and send email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userEmail);
            mailMessage.setFrom("tashiniwijethunga@gmail.com");
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            javaMailSender.send(mailMessage);
            return "Receipt sent successfully";
        } catch (MailException e) {
            throw new RuntimeException("Failed to send receipt email: " + e.getMessage(), e);
        }
    }
}
