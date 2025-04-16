package lk.ijse.Salone.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private UUID   uid;
    private String email;
    private String name;
    private String password;
    private String role;
    private String primary_phone_number;
    private String fileName;
    private String filetype;
    private byte[] data;
    private String profilePicture;
    private boolean verified;
    private String verificationCode;
    private Date joinDate;
}
