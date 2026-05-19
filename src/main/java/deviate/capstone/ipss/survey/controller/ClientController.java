package deviate.capstone.ipss.survey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deviate.capstone.ipss.survey.Dto.request.ClientRegistrationDto;
import deviate.capstone.ipss.survey.Dto.request.ClientRequestDto;
import deviate.capstone.ipss.survey.Dto.response.ClientResponseDto;
import deviate.capstone.ipss.survey.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/client")
@RequiredArgsConstructor
public class ClientController {
    
    private final SurveyService surveyService;

    @PostMapping("register")
    public ResponseEntity<String> registerPotentialBusiness(@Valid @RequestBody ClientRegistrationDto request) {
        return surveyService.registerPotentialBusiness(request);
    }

    @GetMapping("list/all")
    public List<ClientResponseDto> getAllClients() {
        return surveyService.returnAllClients();
    }

    @GetMapping("list/one")
    public ClientResponseDto getClient(@Valid @RequestBody ClientRequestDto request) {
        return surveyService.returnClient(request);
    }
}
