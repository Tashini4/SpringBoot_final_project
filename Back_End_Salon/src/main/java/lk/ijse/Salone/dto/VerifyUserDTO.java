package lk.ijse.Salone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyUserDTO {
    private String email;
    private String code;
    private String Nic;
}
