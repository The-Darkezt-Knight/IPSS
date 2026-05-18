package deviate.capstone.ipss.survey.service;

import java.util.List;

import org.springframework.stereotype.Service;

import deviate.capstone.ipss.shared.ResourceNotFoundException;
import deviate.capstone.ipss.survey.Dto.request.LocationRequestDto;
import deviate.capstone.ipss.survey.Dto.response.BaranggayResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.CityMunicipalityResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.ProvinceResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.RegionResponseDto;
import deviate.capstone.ipss.survey.entity.location.CityMunicipality;
import deviate.capstone.ipss.survey.entity.location.Province;
import deviate.capstone.ipss.survey.entity.location.Region;
import deviate.capstone.ipss.survey.repository.location.BaranggayRepository;
import deviate.capstone.ipss.survey.repository.location.CityMunicipalityRepository;
import deviate.capstone.ipss.survey.repository.location.ProvinceRepository;
import deviate.capstone.ipss.survey.repository.location.RegionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {
    
    private final RegionRepository regionRepository;
    private final ProvinceRepository provinceRepository;
    private final CityMunicipalityRepository cityMunicipalityRepository;
    private final BaranggayRepository baranggayRepository;


    //[0]Returns all regions
    public List<RegionResponseDto> returnRegions() {
        return regionRepository.findAll()
            .stream().map(r -> new RegionResponseDto(r.getCode(), r.getName(), r.getRegionCode()))
                .toList();
    }

    //[1]returns the region code by region name
    public String getRegionCodeByName(String name){
        Region region = regionRepository.findByName(name)
            .orElseThrow(()-> new ResourceNotFoundException("REGION", name));

        String code = region.getCode();

        return code;
    }
    

    //[2]Returns provinces by region code
    public List<ProvinceResponseDto> returnProvincesByRegion(LocationRequestDto request) {
        return provinceRepository.findAllByRegionCode(getRegionCodeByName(request.regionName()))
            .stream().map(p -> new ProvinceResponseDto(p.getCode(), p.getName(), p.getProvinceCode())).toList();
    }


    //[3]returns the province code by province name
    public String getProvinceCodeByName(String name) {
        Province province = provinceRepository.findByName(name)
            .orElseThrow(()-> new ResourceNotFoundException("PROVINCE", name));

        String code = province.getCode();
        
        return code;
    }


    //[4]Returns cities/municipalities by province code
    public List<CityMunicipalityResponseDto> returnCitiesMunicipaliesByProvince(LocationRequestDto request) {
        return cityMunicipalityRepository.findAllByProvinceCode(getProvinceCodeByName(request.provinceName()))
            .stream().map(cm -> new CityMunicipalityResponseDto(cm.getCode(), cm.getName(), cm.getCityMunicipalityCode())).toList();
    }


    //[5]returns the city/ municipality code by city/mmunicipality name
    public String getCityMunicipalityCodeByName(String name) {
        CityMunicipality cityMunicipality = cityMunicipalityRepository.findByName(name)
            .orElseThrow(()-> new ResourceNotFoundException("CITY/MUNICIPALITY", name));

        String code = cityMunicipality.getCode();

        return code;
    }


    //[6]Returns Baranggays by city/municipality code
    public List<BaranggayResponseDto> returnBaranggayByCity(LocationRequestDto request) {
        return baranggayRepository.findAllByCityCode(getCityMunicipalityCodeByName(request.cityMunicipalityName()))
            .stream().map(b-> new BaranggayResponseDto(b.getCode(), b.getName(), b.getBaranggayCode())).toList();
    }



}
