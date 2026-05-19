#April 20

I left at the listing of users. It's partially done but no controllers are made yet.
For the listing, I used mappers(UserMapper) to separate the population of UserResponse data, leaving only the UserService the work for calling the mapper function.

Http request coming in
Controller handles the request(not yet made)
Service class takes the request
Service class calls UserMapper
UserMapper returns the DTO-turned Request
Service class returns to the controller the values\

#April 21
Auth service's basic functionality is finished from the following:
    1.Adding of users
    2.Updating user information
    3.Removing users
    4.Listing of all users
    5.Listing a single user
    6.Activating/ deactivating users

A number of changes must be made including
    1. Refactoring of code
    2. Enhancing database queries
    3. Putting removed accounts into a separate table instead of deleting them from the records

    #12 noon
    Solved initially proposed issues from 12 - 10, 8-7

    Issues regarding CSRF is skipped such as that of issue #9

     Follow-up updates tracked from #12 noon:
     1. src/main/java/deviate/capstone/ipss/app/exception/GlobalExceptionHandler.java
         - Removed Spring Security AccessDeniedException handling.
         - Added custom handlers for UnauthorizedException (401) and ForbiddenException (403).
         - Added structured logging for unhandled and auth-related errors.

     2. src/main/java/deviate/capstone/ipss/auth/component/UserFactory.java
         - Replaced direct Enum.valueOf usage with EnumUtils.parseEnum for sex and role fields.

     3. src/main/java/deviate/capstone/ipss/auth/controller/UserController.java
         - Changed create endpoint to @PostMapping with explicit JSON consumes/produces media types.
         - Updated registration success response text formatting.

     4. src/main/java/deviate/capstone/ipss/auth/service/RegistrationService.java
         - Added regex-based government email validation using GOV_PH_EMAIL pattern.
         - Added constructor null checks with Objects.requireNonNull.
         - Updated validation message for invalid government email.

     5. src/main/java/deviate/capstone/ipss/auth/service/UserService.java
         - Replaced direct enum conversion with EnumUtils.parseEnum for update profile flow.

     6. src/main/java/deviate/capstone/ipss/shared/UnauthorizedException.java
         - Corrected UNAUTHORIZED status code from 400 to 401.

     7. src/main/java/deviate/capstone/ipss/shared/ForbiddenException.java
         - Added new custom ForbiddenException class mapped to status 403.

     8. src/main/java/deviate/capstone/ipss/shared/Utility/EnumUtils.java
         - Added new enum parsing utility with ValidationException fallback for invalid values.

     9. DEVELOPERNOTE.md
         - Added this tracking section to document all updates from #12 noon onward.




#FINDINGS
All six remain open.

Finding 1 status: Open
Severity: Critical
Location: SecurityConfig.java:17, UserController.java:44
Problem: Global permit-all is still active, so all auth-management routes remain publicly callable.
Recommended fix: Require authentication by default and add role-based authorization on each sensitive endpoint.

Finding 2 status: Open
Severity: Critical
Location: PasswordService.java:9, UserFactory.java:33
Problem: Temporary password is still deterministic lastname.govtId and is persisted directly as returned value.
Recommended fix: Generate random temporary credentials and store only hashed passwords via PasswordEncoder.

Finding 3 status: Open
Severity: Critical
Location: UserController.java:44, UserResponse.java:16, UserResponse.java:17
Problem: PII fields govtEmail and govtId are still exposed by list responses, and route is still unauthenticated due Finding 1.
Recommended fix: Restrict access and remove or mask sensitive identifiers in broad list responses.

Finding 4 status: Open
Severity: High
Location: RegistrationService.java:14, RegistrationService.java:34, EnumUtils.java:3, GlobalExceptionHandler.java:44
Problem: You still throw jakarta.validation.ValidationException in service/utility code, but there is no explicit handler for it; it falls into generic 500 handling.
Recommended fix: Throw the project custom validation exception type or add a dedicated handler mapping jakarta ValidationException to 400.

Finding 5 status: Open
Severity: High
Location: SingleUserRequestById.java:4, UserController.java:57
Problem: ID request DTO still has no validation constraints, so null or invalid IDs can still hit service methods and cause runtime failures.
Recommended fix: Add NotNull and Positive style constraints to the id field and keep consistent 400 responses on invalid input.

Finding 6 status: Open
Severity: High
Location: User.java:16, RegistrationService.java:37, RegistrationService.java:46
Problem: Registration still uses check-then-insert without DB-level uniqueness constraints; concurrent requests can create duplicates.
Recommended fix: Enforce unique constraints/indexes on govtEmail and govtId, then handle integrity violations cleanly.


#April 22
#7pm onward

Implemented and updated the following:

1. Login API flow (JWT-based) was added.
    - src/main/java/deviate/capstone/ipss/auth/Dto/Requests/LoginRequest.java
        - Added login payload with govtEmail and password validation.
    - src/main/java/deviate/capstone/ipss/auth/Dto/Responses/LoginResponse.java
        - Added login response shape for token return to client.
    - src/main/java/deviate/capstone/ipss/auth/service/AuthService.java
        - Added login validation flow: lookup by email, password check, account active check, token generation.
    - src/main/java/deviate/capstone/ipss/auth/controller/LoginController.java
        - Added POST api/auth/login endpoint returning LoginResponse.
    - src/main/java/deviate/capstone/ipss/auth/repository/UserRepository.java
        - Added findByGovtEmail for login lookup.

2. JWT support and startup configuration were added.
    - src/main/java/deviate/capstone/ipss/auth/service/JwtService.java
        - Added JWT generation, token validation, and token subject extraction.
    - src/main/resources/application.properties
        - Added jwt.secret runtime property.
    - pom.xml
        - Added JJWT dependencies (jjwt-api, jjwt-impl, jjwt-jackson).

3. Exception and validation handling was strengthened.
    - src/main/java/deviate/capstone/ipss/app/exception/GlobalExceptionHandler.java
        - Added handling for ValidationException -> 400.
        - Added handling for DataIntegrityViolationException -> 409 with duplicate key mapping.
    - src/main/java/deviate/capstone/ipss/auth/service/RegistrationService.java
        - Replaced jakarta ValidationException with project ValidationException.
    - src/main/java/deviate/capstone/ipss/shared/Utility/EnumUtils.java
        - Replaced jakarta ValidationException with project ValidationException.

4. Request validation and persistence constraints were tightened.
    - src/main/java/deviate/capstone/ipss/auth/Dto/Requests/SingleUserRequestById.java
        - Added @NotNull and @Positive validation for id.
    - src/main/java/deviate/capstone/ipss/auth/entity/User.java
        - Added DB unique constraints for govt_email and govt_id.

5. Password encoding support was updated.
    - src/main/java/deviate/capstone/ipss/app/configuration/SecurityConfig.java
        - Added PasswordEncoder bean (BCryptPasswordEncoder).
    - src/main/java/deviate/capstone/ipss/auth/service/PasswordService.java
        - Updated temporary password creation to return hashed password.

6. Other tracked updates in the same timeframe.
    - src/main/java/deviate/capstone/ipss/shared/ResourceNotFoundException.java
        - Added email-based constructor overload.
    - src/main/java/deviate/capstone/ipss/auth/Dto/Responses/UserResponse.java
        - Added password field in response model.
    - src/main/java/deviate/capstone/ipss/auth/component/UserMapper.java
        - Added password mapping to UserResponse.


What's left:
    I need to close all endpoints, and ensure that only the right people can have access to particular requests.


#May 17, 2026

Implemented and updated the following:

1. Security config adjustment for survey client routes.
    - src/main/java/deviate/capstone/ipss/app/configuration/SecurityConfig.java
        - Added /api/client/** to permit-all list (temporary access for client endpoints).

2. Survey module scaffolding and domain models.
    - src/main/java/deviate/capstone/ipss/survey/component/ClientFactory.java
        - Added client factory for survey domain.
    - src/main/java/deviate/capstone/ipss/survey/component/ClientMapper.java
        - Added mapper for client registration/response.
    - src/main/java/deviate/capstone/ipss/survey/controller/ClientController.java
        - Added controller for client survey endpoints.
    - src/main/java/deviate/capstone/ipss/survey/service/SurveyService.java
        - Added survey service layer.
    - src/main/java/deviate/capstone/ipss/survey/Dto/request/ClientRegistrationDto.java
        - Added client registration payload.
    - src/main/java/deviate/capstone/ipss/survey/Dto/request/AssistanceRegistrationRepository.java
        - Added assistance registration request model.
    - src/main/java/deviate/capstone/ipss/survey/Dto/response/ClientResponseDto.java
        - Added client response model.
    - src/main/java/deviate/capstone/ipss/survey/entity/Client.java
        - Added client entity.
    - src/main/java/deviate/capstone/ipss/survey/entity/Assistance.java
        - Added assistance entity.
    - src/main/java/deviate/capstone/ipss/survey/entity/asst/AssistanceType.java
        - Added assistance type entity.
    - src/main/java/deviate/capstone/ipss/survey/entity/asst/AssistanceSubtype.java
        - Added assistance subtype entity.
    - src/main/java/deviate/capstone/ipss/survey/entity/classification/CivilStatus.java
        - Added civil status classification entity.
    - src/main/java/deviate/capstone/ipss/survey/entity/classification/MSMEClassification.java
        - Added MSME classification entity.
    - src/main/java/deviate/capstone/ipss/survey/entity/location/Region.java
        - Added region entity.
    - src/main/java/deviate/capstone/ipss/survey/entity/location/Province.java
        - Added province entity.
    - src/main/java/deviate/capstone/ipss/survey/entity/location/CityMunicipality.java
        - Added city/municipality entity.
    - src/main/java/deviate/capstone/ipss/survey/entity/location/Baranggay.java
        - Added barangay entity.
    - src/main/java/deviate/capstone/ipss/survey/repository/ClientRepository.java
        - Added client repository.
    - src/main/java/deviate/capstone/ipss/survey/repository/AssistanceRepository.java
        - Added assistance repository.
    - src/main/java/deviate/capstone/ipss/survey/repository/asst/AssistanceTypeRepository.java
        - Added assistance type repository.
    - src/main/java/deviate/capstone/ipss/survey/repository/asst/AssistanceSubtypeRepository.java
        - Added assistance subtype repository.
    - src/main/java/deviate/capstone/ipss/survey/repository/location/RegionRepository.java
        - Added region repository.
    - src/main/java/deviate/capstone/ipss/survey/repository/location/ProvinceRepository.java
        - Added province repository.
    - src/main/java/deviate/capstone/ipss/survey/repository/location/CityMunicipalityRepository.java
        - Added city/municipality repository.
    - src/main/java/deviate/capstone/ipss/survey/repository/location/BaranggayRepository.java
        - Added barangay repository.

3. Seed data for location tables.
    - src/main/resources/data/LocationSeeder.sql
        - Added location seed SQL for regions/provinces/cities/barangays.