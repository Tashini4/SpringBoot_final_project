package lk.ijse.Salone.service.custom.IMPL;

import jakarta.transaction.Transactional;
import lk.ijse.Salone.Entity.Stylist;
import lk.ijse.Salone.dto.StylistDTO;
import lk.ijse.Salone.repo.StylistRepository;
import lk.ijse.Salone.service.custom.StylistService;
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
public class StylistServiceImpl implements StylistService {
    @Autowired
    private StylistRepository stylistRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public int saveStylist(StylistDTO stylistDTO, MultipartFile file) throws IOException {
       Stylist stylist = new Stylist();
               stylist = modelMapper.map(stylistDTO,Stylist.class);
        if (file != null && !file.isEmpty()){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            stylist.setFileName(fileName);
            stylist.setFiletype(file.getContentType());
            stylist.setData(file.getBytes());
        }
       stylistRepository.save(stylist);
       return VarList.Created;

    }

    @Override
    public int updateStylist(StylistDTO stylistDTO) {
       try{
           if (stylistRepository.existsById(stylistDTO.getStylistId())){
               Stylist stylist = modelMapper.map(stylistDTO,Stylist.class);
               stylistRepository.save(stylist);
               return VarList.OK;
           }else{
               return VarList.Not_Found;
           }
       }catch (Exception e){
           return VarList.Bad_Gateway;
       }
    }

    @Override
    public List<StylistDTO> getStylist() {
       List<Stylist> stylistList = stylistRepository.findAll();
       return stylistList.stream()
               .map(stylist -> modelMapper.map(stylist,StylistDTO.class))
               .collect(Collectors.toList());

    }

    @Override
    public StylistDTO getStylistById(int serviceId) {
        Stylist stylist = stylistRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Stylist not found"));
        return  modelMapper.map(stylist,StylistDTO.class);
    }
    @Override
    public void deleteStylist(int stylistId) {
        stylistRepository.deleteById(stylistId);
    }
}
