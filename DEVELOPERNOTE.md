I left at the listing of users. It's partially done but no controllers are made yet.
For the listing, I used mappers(UserMapper) to separate the population of UserResponse data, leaving only the UserService the work for calling the mapper function.

Http request coming in
Controller handles the request(not yet made)
Service class takes the request
Service class calls UserMapper
UserMapper returns the DTO-turned Request
Service class returns to the controller the values