package lk.ijse.Salone.controller;

import jakarta.validation.Valid;
import lk.ijse.Salone.dto.AuthDTO;
import lk.ijse.Salone.dto.ResponseDTO;
import lk.ijse.Salone.dto.UserDTO;
import lk.ijse.Salone.dto.VerifyUserDTO;
import lk.ijse.Salone.repo.UserRepository;
import lk.ijse.Salone.service.custom.UserService;
import lk.ijse.Salone.util.JwtUtil;
import lk.ijse.Salone.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    //constructor injection
    public UserController(UserService userService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("test");
        return "test";
    }

    @GetMapping("/get4Users")
    public ResponseEntity<ResponseDTO> getTop4Users() {
        System.out.println("get 4 Users");
      try{ List<UserDTO> users = userService.getLast4Users();
          if (users.isEmpty()) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND)
                      .body(new ResponseDTO(VarList.Not_Found, "Users Not Found", null));
          }else{
              return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", users));

          }} catch (Exception e) {
          throw new RuntimeException(e);
      }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<ResponseDTO> getAllUsers() {
        try {
            List<UserDTO> users = userService.getUsers();
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
        System.out.println("register wwwwwwwwwwwwwwwwwwwwww");
        System.out.println(userDTO.getEmail());
        System.out.println(userDTO.getName());
        System.out.println(userDTO.getRole());
        try {
            //get Local date
            LocalDate localDate = LocalDate.now();
            Date JoinDate = Date.valueOf(localDate);
            userDTO.setJoinDate(JoinDate);
            int res = userService.saveUser(userDTO);
            switch (res) {
                case VarList.Created -> {
                    String token = jwtUtil.generateToken(userDTO);
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(userDTO.getEmail());
                    authDTO.setToken(token);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", authDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @PostMapping(value = "/falseNullUser")
    public ResponseEntity<ResponseDTO> falseNullUser(@RequestBody VerifyUserDTO verifyUserDTO) {
        try {
            int res = userService.CodeSent(verifyUserDTO.getEmail());
            switch (res) {
                case VarList.OK:
                    System.out.println("User verified code sent");
                    return ResponseEntity.ok(new ResponseDTO(VarList.OK, "User Verified Successfully", verifyUserDTO));
                case VarList.Not_Found:
                    System.out.println("User not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "User Not Found", null));
                default:
                    System.out.println("Error verifying user");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseDTO(VarList.Internal_Server_Error, "Error verifying user", null));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
