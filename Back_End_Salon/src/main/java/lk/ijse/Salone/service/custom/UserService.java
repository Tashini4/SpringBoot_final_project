package lk.ijse.Salone.service.custom;


import lk.ijse.Salone.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    //get last 4 users
    List<UserDTO> getLast4Users();
    int verifyUser(String email,String code);

    int saveUser(UserDTO userDTO);

     UserDTO searchUser(String username);

     int updateUser(UserDTO userDTO);

     int verifyUser2(String email,String code);

     int updateUser(UserDTO userDTO,MultipartFile multipartFile);

     int updateUser2(UserDTO userDTO);

     List<UserDTO> getUsers();

    int deleteUser(String email);

    int CodeSent(String email);
}