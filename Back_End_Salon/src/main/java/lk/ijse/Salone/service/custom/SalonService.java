package lk.ijse.Salone.service.custom;

import lk.ijse.Salone.dto.SalonDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SalonService {
    int saveService(SalonDTO salonDTO, MultipartFile multipartFile) throws IOException;
    int updateService(SalonDTO salonDTO);
    List<SalonDTO> getAllService();
    void deleteService(int serviceId);
    SalonDTO getServiceById(int serviceId);
}
