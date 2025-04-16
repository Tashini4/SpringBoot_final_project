package lk.ijse.Salone.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
    private String primary_phone_number;
    //profile picture upload
    private String fileName;
    private String filetype;
    @Lob
    private byte[] data;

    private boolean verified;
    private String verificationCode;

    private Date joinDate;

}
