package lk.ijse.Salone.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name= "salon")
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceId;
    private String serviceName;
    private double servicePrice;
    private String serviceDescription;
    private String fileName;
    private String filetype;
    @Lob
    private byte[] data;
    private Integer duration;
}
