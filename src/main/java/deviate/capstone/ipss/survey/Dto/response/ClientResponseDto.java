package deviate.capstone.ipss.survey.Dto.response;

import java.time.LocalDate;

import deviate.capstone.ipss.survey.Dto.response.location.CityMunicipalityResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.ProvinceResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.RegionResponseDto;
import deviate.capstone.ipss.survey.entity.classification.CivilStatus;
import deviate.capstone.ipss.survey.entity.classification.MSMEClassification;

public record ClientResponseDto(
    String id,
    String oldId,

    String statusOfClient,    // e.g. "Potential"
    String specifyLevel,
    String categoryOfClient,
    String socialClassification,
    String diffAbledType,
    Boolean isSenior,
    Boolean isIndigeneous,
    String levelOfDigitalization,
    String digitalTools,
    MSMEClassification msmeClassification,
    String clientDesignation,

    String firstName,
    String middleName,
    String lastName,
    String suffix,
    CivilStatus civilStatus,
    String sex,
    LocalDate birthdate,
    Integer birthYear,
    String citizenship,

    String dtiKonekId,
    String philippineIdentificationSystem,

    RegionResponseDto region,
    ProvinceResponseDto province,
    CityMunicipalityResponseDto cityMunicipality,
    BaranggayResponseDto baranggay,
    
    //Geo table - Location
    String district,
    String zipCode,
    String address,
    Double latitude,
    Double longitude,

    //Contact
    String landlineNumber,
    String faxNumber,
    String mobileNumber,
    String emailAddress,
    String socialMedia,
    String website,
    String eCommercePlatform
) {
}
