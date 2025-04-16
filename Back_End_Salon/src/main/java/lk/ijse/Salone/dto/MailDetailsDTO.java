package lk.ijse.Salone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MailDetailsDTO {
    private String toMail;
    private String message;
    private String subject;
}
