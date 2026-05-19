package deviate.capstone.ipss.survey.component;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import deviate.capstone.ipss.survey.Dto.request.ClientRegistrationDto;
import deviate.capstone.ipss.survey.entity.Client;
import deviate.capstone.ipss.survey.entity.location.Baranggay;
import deviate.capstone.ipss.survey.entity.location.CityMunicipality;
import deviate.capstone.ipss.survey.entity.location.Province;
import deviate.capstone.ipss.survey.entity.location.Region;

@Component
public class ClientFactory {
    
    public Client createClientInformation(
        ClientRegistrationDto request,
        Baranggay baranggay,
        CityMunicipality cityMunicipality,
        Province province,
        Region region
    ) {
        return Client.builder()
        .clientId(request.id())
        .oldClientId(request.oldId())
        .dateCreated(LocalDateTime.now())
        .statusOfClient(request.statusOfClient())
        .specifyLevel(request.specifyLevel())
        .categoryOfClient(request.categoryOfClient())
        .socialClassification(request.socialClassification())
        .diffAbledType(request.diffAbledType())
        .isSenior(request.isSenior())
        .isIndigeneous(request.isIndigeneous())
        .levelOfDigitalization(request.levelOfDigitalization())
        .digitalTools(request.digitalTools())
        .msmeClassification(request.msmeClassification())
        .clientDesignation(request.clientDesignation())
        
        .firstName(request.firstName())
        .middleName(request.middleName())
        .lastName(request.lastName())
        .suffix(request.suffix())
        .civilStatus(request.civilStatus())
        .sex(request.sex())
        .birthdate(request.birthdate())
        .birthYear(request.birthYear())
        .citizenship(request.citizenship())

        .dtiKonekId(request.dtiKonekId())
        .philippineIdentificationSystem(request.philippineIdentificationSystem())

        .region(region)
        .province(province)
        .cityMunicipality(cityMunicipality)
        .baranggay(baranggay)

        .district(request.district())
        .zipCode(request.zipCode())
        .address(request.address())
        .latitude(request.latitude())
        .longitude(request.longitude())

        .landlineNumber(request.landlineNumber())
        .faxNumber(request.faxNumber())
        .mobileNumber(request.mobileNumber())
        .emailAddress(request.emailAddress())
        .socialMedia(request.socialMedia())
        .website(request.website())
        .eCommercePlatform(request.eCommercePlatform())
        .build();

    }
}
