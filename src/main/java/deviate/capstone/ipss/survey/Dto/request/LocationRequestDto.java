package deviate.capstone.ipss.survey.Dto.request;

import jakarta.annotation.Nullable;

public record LocationRequestDto(
    @Nullable
    String regionName,
    @Nullable
    String provinceName,
    @Nullable
    String districtName,
    @Nullable
    String cityMunicipalityName,
    @Nullable
    String baranggayName,
    @Nullable
    String code
) {
    
}
