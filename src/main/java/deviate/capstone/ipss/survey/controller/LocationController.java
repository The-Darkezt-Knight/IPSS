package deviate.capstone.ipss.survey.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deviate.capstone.ipss.survey.Dto.request.LocationRequestDto;
import deviate.capstone.ipss.survey.Dto.response.BaranggayResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.CityMunicipalityResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.ProvinceResponseDto;
import deviate.capstone.ipss.survey.Dto.response.location.RegionResponseDto;
import deviate.capstone.ipss.survey.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/location")
@RequiredArgsConstructor
public class LocationController {
    
    private final LocationService locationService;

    //returns a list of all regions
    @GetMapping("region/all")
    public List<RegionResponseDto> returnRegions() {
        return locationService.returnRegions();
    }

    //returns the region code by name
    @PostMapping("region/name")
    public String getRegionCodeByName(@Valid @RequestBody LocationRequestDto request) {
        return locationService.getRegionCodeByName(request.regionName());
    }

    //returns a list of all provinces in a region
    @PostMapping("province/code")
    public List<ProvinceResponseDto> returnProvincesByCode(@Valid @RequestBody LocationRequestDto request) {
        return locationService.returnProvincesByRegion(request);
    }

    //returns the province code by name
    @PostMapping("province/name")
    public String getProvinceCodeByName(@Valid @RequestBody LocationRequestDto request) {
        return locationService.getProvinceCodeByName(request.provinceName());
    }

    //returns a list of cities/municipalities in a province
    @PostMapping("cityMunicipality/code")
    public List<CityMunicipalityResponseDto> returnCitiesMunicipalitiesByProvince(@Valid @RequestBody LocationRequestDto request) {
        return locationService.returnCitiesMunicipaliesByProvince(request);
    }
    

    //returns the city/municipality code by name
    @PostMapping("cityMunicipality/name")
    public String getCityMunicipalityCodeByName(@Valid @RequestBody LocationRequestDto request) {
        return locationService.getCityMunicipalityCodeByName(request.cityMunicipalityName());
    }

    //returns a list of all baranggay in a city/municipalit
    @PostMapping("baranggay/all")
    public List<BaranggayResponseDto> returnBaranggayByCityMunicipality(@Valid @RequestBody LocationRequestDto request) {
        return locationService.returnBaranggayByCity(request);
    }


}
