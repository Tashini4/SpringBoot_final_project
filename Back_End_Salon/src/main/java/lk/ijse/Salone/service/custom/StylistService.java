package lk.ijse.Salone.service.custom;

import lk.ijse.Salone.dto.StylistDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StylistService {
    int saveStylist(StylistDTO stylistDTO, MultipartFile file) throws IOException;
    int updateStylist(StylistDTO stylistDTO);
    List<StylistDTO> getStylist();

    StylistDTO getStylistById(int serviceId);

    void deleteStylist(int stylistId);
}
