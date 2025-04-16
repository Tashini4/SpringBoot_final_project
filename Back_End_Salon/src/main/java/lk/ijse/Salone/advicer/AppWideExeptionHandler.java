package lk.ijse.Salone.advicer;

import lk.ijse.Salone.dto.ResponseDTO;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExeptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseDTO ExeptionHandler(Exception e){
        return new ResponseDTO(500, "Internal Server Error", e.getMessage());
    }
}
