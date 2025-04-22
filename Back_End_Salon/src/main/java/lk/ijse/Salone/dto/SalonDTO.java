package lk.ijse.Salone.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class  SalonDTO {
    private int serviceId;
    private String serviceName;
    private double servicePrice;
    private String serviceDescription;
    private String fileName;
    private String filetype;
    private byte[] data;
    private Integer duration;
}
