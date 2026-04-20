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

A number of changes must be made including
    1. Refactoring of code
    2. Enhancing database queries
    3. Putting removed accounts into a separate table instead of deleting them from the records