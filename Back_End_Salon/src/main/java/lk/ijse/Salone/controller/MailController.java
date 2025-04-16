package lk.ijse.Salone.controller;

import lk.ijse.Salone.dto.MailDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mail")
public class MailController {
    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping(value = "/send")
    public String sendMail(@RequestBody MailDetailsDTO mailDetailsDTO) {
try {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(mailDetailsDTO.getToMail());
    simpleMailMessage.setFrom("tashiniwijethunga@gmail.com");
    simpleMailMessage.setSubject(mailDetailsDTO.getSubject());
    simpleMailMessage.setText(mailDetailsDTO.getMessage());
    javaMailSender.send(simpleMailMessage);
    return "Mail Send Success";
} catch (MailException e) {
    throw new RuntimeException(e);
}
    }
}
