package deviate.capstone.ipss.survey.component;

import org.springframework.stereotype.Component;

import deviate.capstone.ipss.survey.Dto.response.BaranggayResponseDto;
import deviate.capstone.ipss.survey.Dto.response.ClientResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.CityMunicipalityResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.ProvinceResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.RegionResponseDto;
import deviate.capstone.ipss.survey.entity.Client;
import deviate.capstone.ipss.survey.entity.location.Baranggay;
import deviate.capstone.ipss.survey.entity.location.CityMunicipality;
import deviate.capstone.ipss.survey.entity.location.Province;
import deviate.capstone.ipss.survey.entity.location.Region;

@Component
public class ClientMapper {
    
    public ClientResponseDto toDto(Client client) {
        return new ClientResponseDto(
            client.getClientId(),
            client.getOldClientId(),

            client.getStatusOfClient(),
            client.getSpecifyLevel(),
            client.getCategoryOfClient(),
            client.getSocialClassification(),
            client.getDiffAbledType(),
            client.getIsSenior(),
            client.getIsIndigeneous(),
            client.getLevelOfDigitalization(),
            client.getDigitalTools(),
            client.getMsmeClassification(),
            client.getClientDesignation(),

            client.getFirstName(),
            client.getMiddleName(),
            client.getLastName(),
            client.getSuffix(),
            client.getCivilStatus(),
            client.getSex(),
            client.getBirthdate(),
            client.getBirthYear(),
            client.getCitizenship(),

            client.getDtiKonekId(),
            client.getPhilippineIdentificationSystem(),

            toRegionDto(client.getRegion()),
            toProvinceDto(client.getProvince()),
            toCityMunicipalityDto(client.getCityMunicipality()),
            toBaranggayDto(client.getBaranggay()),

            client.getDistrict(),
            client.getZipCode(),
            client.getAddress(),
            client.getLatitude(),
            client.getLongitude(),

            client.getLandlineNumber(),
            client.getFaxNumber(),
            client.getMobileNumber(),
            client.getEmailAddress(),
            client.getSocialMedia(),
            client.getWebsite(),
            client.getECommercePlatform()
        );
    }

    private RegionResponseDto toRegionDto(Region region) {
        if (region == null) {
            return null;
        }

        return new RegionResponseDto(
            region.getCode(),
            region.getName(),
            region.getRegionCode()
        );
    }

    private ProvinceResponseDto toProvinceDto(Province province) {
        if (province == null) {
            return null;
        }

        return new ProvinceResponseDto(
            province.getCode(),
            province.getName(),
            province.getProvinceCode()
        );
    }

    private CityMunicipalityResponseDto toCityMunicipalityDto(CityMunicipality cityMunicipality) {
        if (cityMunicipality == null) {
            return null;
        }

        return new CityMunicipalityResponseDto(
            cityMunicipality.getCode(),
            cityMunicipality.getName(),
            cityMunicipality.getCityMunicipalityCode()
        );
    }

    private BaranggayResponseDto toBaranggayDto(Baranggay baranggay) {
        if (baranggay == null) {
            return null;
        }

        return new BaranggayResponseDto(
            baranggay.getCode(),
            baranggay.getName(),
            baranggay.getBaranggayCode()
        );
    }
}
