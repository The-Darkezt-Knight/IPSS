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