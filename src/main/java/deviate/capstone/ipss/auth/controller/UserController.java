package deviate.capstone.ipss.auth.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deviate.capstone.ipss.auth.Dto.Requests.RegisterRequest;
import deviate.capstone.ipss.auth.Dto.Requests.SingleUserRequestById;
import deviate.capstone.ipss.auth.Dto.Requests.UpdateUserProfileRequest;
import deviate.capstone.ipss.auth.Dto.Responses.UserResponse;
import deviate.capstone.ipss.auth.service.RegistrationService;
import deviate.capstone.ipss.auth.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/user")
public class UserController {
    
    private final RegistrationService registrationService;
    private final UserService userService;

    public UserController(RegistrationService registrationService, UserService userService) {
        this.registrationService = Objects.requireNonNull(registrationService);
        this.userService = Objects.requireNonNull(userService);
    }

    @RequestMapping("create")
    public ResponseEntity<String> registerStaff(@Valid @RequestBody RegisterRequest request) {
        registrationService.registerStaff(request);

        return ResponseEntity.status(HttpStatus.OK)
        .body("Successfully registered " + request.firstName()
        .concat(" " + request.lastName().trim()  + "as an/a " + request.role()));
    }

    @GetMapping("list/all")
    public List<UserResponse> returnAllUsersInAList() {
        return userService.getAllUsers();
    }

    @PostMapping("list/one")
    public ResponseEntity<UserResponse> returnUserById(@Valid @RequestBody SingleUserRequestById request) {
        
        return ResponseEntity.ok(userService.getUser(request.id()));
    }

    @PostMapping("set/status")
    public ResponseEntity<String> setUserStatus(@Valid @RequestBody SingleUserRequestById request) {
        userService.setUserStatus(request.id());

        return ResponseEntity.status(HttpStatus.OK)
                .body("Successfully set user status");
        
    }

    @PostMapping("remove")
    public ResponseEntity<String> deleteUserById(@Valid @RequestBody SingleUserRequestById request) {
        userService.removeUserById(request.id());

        return ResponseEntity.status(HttpStatus.OK)
            .body("Successfully removed a user");
    }

    @PostMapping("update")
    public String updateUserProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        return userService.updateUserProfile(request);
    }

}
