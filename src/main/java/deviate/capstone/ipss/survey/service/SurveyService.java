package deviate.capstone.ipss.survey.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import deviate.capstone.ipss.shared.ResourceNotFoundException;
import deviate.capstone.ipss.survey.Dto.request.ClientRegistrationDto;
import deviate.capstone.ipss.survey.Dto.request.ClientRequestDto;
import deviate.capstone.ipss.survey.Dto.response.ClientResponseDto;
import deviate.capstone.ipss.survey.component.ClientFactory;
import deviate.capstone.ipss.survey.component.ClientMapper;
import deviate.capstone.ipss.survey.entity.Client;
import deviate.capstone.ipss.survey.entity.location.Baranggay;
import deviate.capstone.ipss.survey.entity.location.CityMunicipality;
import deviate.capstone.ipss.survey.entity.location.Province;
import deviate.capstone.ipss.survey.entity.location.Region;
import deviate.capstone.ipss.survey.repository.ClientRepository;
import deviate.capstone.ipss.survey.repository.location.BaranggayRepository;
import deviate.capstone.ipss.survey.repository.location.CityMunicipalityRepository;
import deviate.capstone.ipss.survey.repository.location.ProvinceRepository;
import deviate.capstone.ipss.survey.repository.location.RegionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {
    
    private final ClientRepository clientRepository;
    private final RegionRepository regionRepository;
    private final ProvinceRepository provinceRepository;
    private final CityMunicipalityRepository cityMunicipalityRepository;
    private final BaranggayRepository baranggayRepository;
    //private final BaranggayRepository baranggayRepository;
    private final ClientFactory clientFactory;
    private final ClientMapper clientMapper;

    //Saves a client
    public ResponseEntity<String> registerPotentialBusiness(ClientRegistrationDto request) {

        Region region = regionRepository.findByCode(request.regionCode())
            .orElseThrow(()-> new ResourceNotFoundException("Region", request.regionCode()));

        Province province = provinceRepository.findByCode(request.provinceCode())
            .orElseThrow(()-> new ResourceNotFoundException("Province", request.provinceCode()));

        CityMunicipality cityMunicipality = cityMunicipalityRepository.findByCode(request.cityMunicipalityCode())
            .orElseThrow(()-> new ResourceNotFoundException("City/CityMunicipality", request.cityMunicipalityCode()));

        Baranggay baranggay = baranggayRepository.findByCode(request.baranggayCode())
            .orElseThrow(()-> new ResourceNotFoundException("Baranggay", request.baranggayCode()));
        
        Client newClient = clientFactory.createClientInformation(request, baranggay, cityMunicipality, province, region);
        clientRepository.save(newClient);

        return ResponseEntity.status(HttpStatus.OK).body("Successfully registered the business");
    }


    //returns a list of all clients
    public List<ClientResponseDto> returnAllClients() {
        return clientRepository.findAll()
            .stream().map(client -> clientMapper.toDto(client)).toList();
    }

    //returns a single client
    public ClientResponseDto returnClient(ClientRequestDto request) {
        Client client = clientRepository.findByClientId(request.clientId())
            .orElseThrow(()-> new ResourceNotFoundException("CLIENT", request.clientId()));

        return clientMapper.toDto(client);
    }

}
