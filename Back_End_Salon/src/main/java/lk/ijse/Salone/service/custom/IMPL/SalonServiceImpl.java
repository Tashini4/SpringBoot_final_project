package lk.ijse.Salone.service.custom.IMPL;

import jakarta.transaction.Transactional;
import lk.ijse.Salone.Entity.Salon;
import lk.ijse.Salone.dto.SalonDTO;
import lk.ijse.Salone.repo.SalonServiceRepository;
import lk.ijse.Salone.service.custom.SalonService;
import lk.ijse.Salone.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalonServiceImpl implements SalonService {
    @Autowired
    private SalonServiceRepository salonServiceRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public int saveService(SalonDTO salonDTO , MultipartFile multipartFile) throws IOException {
        Salon salon = new Salon();
        salon= modelMapper.map(salonDTO,Salon.class);
        if (multipartFile != null && !multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            salon.setFileName(fileName);
            salon.setFiletype(multipartFile.getContentType());
            salon.setData(multipartFile.getBytes());
        }
        salonServiceRepository.save(salon);
        return VarList.Created;
    }

    @Override
    public int updateService(SalonDTO salonDTO) {
       try{
           if (salonServiceRepository.existsById(salonDTO.getServiceId())){
               Salon salon = modelMapper.map(salonDTO,Salon.class);
               salonServiceRepository.save(salon);
               return VarList.OK;
           }else {
               return VarList.Not_Found;
           }
       }catch (Exception e){
           return VarList.Bad_Gateway;
       }
    }

    @Override
    public List<SalonDTO> getAllService() {
        List<Salon> salons = salonServiceRepository.findAll();
        return salons.stream().map(salon -> modelMapper.map(salon,SalonDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteService(int serviceId) {
            salonServiceRepository.deleteById(serviceId);
    }

    @Override
    public SalonDTO getServiceById(int serviceId) {
        Salon salon = salonServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        return  modelMapper.map(salon,SalonDTO.class);
    }
}
