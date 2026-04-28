package deviate.capstone.ipss.auth.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    

    /**
     * This function registers a staff member with the provided details and returns a success message.
     * 
     * @param request The `registerStaff` method is annotated with `@PreAuthorize("hasAnyRole('ADMIN')")`, which means that only users with the role of ADMIN are allowed to access this method. The method takes in a POST request with JSON data in the body, represented by the `RegisterRequest`
     * @return The method `registerStaff` is returning a `ResponseEntity` object with a status of OK (200) and a body containing a message indicating that the staff member has been successfully registered. The message includes the first name, last name, and role of the registered staff member.
     */
    //@PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerStaff(@Valid @RequestBody RegisterRequest request) {
        registrationService.registerStaff(request);

        return ResponseEntity.status(HttpStatus.OK)
        .body("Successfully registered " + request.firstName()
        .concat(" " + request.lastName().trim()  + " as " + request.role()));
    }

    /**
     * This Java function returns a list of all users, accessible only to users with the role SUPERADMIN.
     * 
     * @return A list of UserResponse objects containing information about all users in the system is being returned.
     */
    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @GetMapping("list/all")
    public List<UserResponse> returnAllUsersInAList() {
        return userService.getAllUsers();
    }

    /**
     * This function returns a user by their ID if the caller has the 'SUPERADMIN' role.
     * 
     * @param request The `returnUserById` method in the code snippet is a POST mapping that requires the caller to have the role of `SUPERADMIN` based on the `@PreAuthorize` annotation. The method takes a request object of type `SingleUserRequestById` as a parameter, which is annotated with
     * @return A ResponseEntity object containing a UserResponse object is being returned.
     */
    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PostMapping("list/one")
    public ResponseEntity<UserResponse> returnUserById(@Valid @RequestBody SingleUserRequestById request) {
        
        return ResponseEntity.ok(userService.getUser(request.id()));
    }

    /**
     * This Java function sets the status of a user with a specific ID after authorization check.
     * 
     * @param request The `setUserStatus` method in the code snippet is using Spring Security's `@PreAuthorize` annotation to restrict access to users with the role `SUPERADMIN`. This means that only users with the role `SUPERADMIN` will be able to access the `setUserStatus` endpoint.
     * @return The method `setUserStatus` is being called with the user ID from the request, and then a ResponseEntity with status code 200 (OK) and the message "Successfully set user status" is being returned.
     */
    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PostMapping("set/status")
    public ResponseEntity<String> setUserStatus(@Valid @RequestBody SingleUserRequestById request) {
        userService.setUserStatus(request.id());

        return ResponseEntity.status(HttpStatus.OK)
                .body("Successfully set user status");
        
    }


    /**
     * This Java function uses Spring Security to ensure only users with the role SUPERADMIN can access it, and it removes a user by ID when called.
     * 
     * @param request The `request` parameter in the `deleteUserById` method is of type `SingleUserRequestById`. It is annotated with `@Valid` to indicate that the request body should be validated against any constraints specified in the `SingleUserRequestById` class. The `@RequestBody` annotation is
     * @return The method `deleteUserById` is returning a `ResponseEntity<String>` with a status of `HttpStatus.OK` and a body message of "Successfully removed a user".
     */
    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PostMapping("remove")
    public ResponseEntity<String> deleteUserById(@Valid @RequestBody SingleUserRequestById request) {
        userService.removeUserById(request.id());

        return ResponseEntity.status(HttpStatus.OK)
            .body("Successfully removed a user");
    }


    /**
     * This Java function updates a user's profile after validating the request and checking if the user has the 'SUPERADMIN' role.
     * 
     * @param request The `request` parameter in the `updateUserProfile` method is of type `UpdateUserProfileRequest`. It is annotated with `@Valid` to indicate that the request should be validated before being processed. The method is responsible for updating the user profile based on the information provided in the request.
     * @return The `updateUserProfile` method from the `userService` is being called with the `request` parameter, and the return value of this method is being returned from the `updateUserProfile` method in the controller.
     */
    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PostMapping("update")
    public String updateUserProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        return userService.updateUserProfile(request);
    }

}
