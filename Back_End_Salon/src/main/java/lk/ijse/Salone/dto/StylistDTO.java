package lk.ijse.Salone.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StylistDTO {
    private int stylistId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String fileName;
    private String filetype;
    private byte[] data;
    private String specialization;
    private String availability;
}
