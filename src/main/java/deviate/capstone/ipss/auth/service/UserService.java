package deviate.capstone.ipss.auth.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import deviate.capstone.ipss.auth.Dto.Requests.UpdateUserProfileRequest;
import deviate.capstone.ipss.auth.Dto.Responses.UserResponse;
import deviate.capstone.ipss.auth.component.UserMapper;
import deviate.capstone.ipss.auth.entity.Role;
import deviate.capstone.ipss.auth.entity.Sex;
import deviate.capstone.ipss.auth.entity.User;
import deviate.capstone.ipss.auth.repository.UserRepository;
import deviate.capstone.ipss.shared.ResourceNotFoundException;
import deviate.capstone.ipss.shared.Utility.EnumUtils;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.userMapper = Objects.requireNonNull(userMapper);
    }

    //returns a list of all the users in the system
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll().stream()
            .map(user -> userMapper.toDto(user)).toList();
    }


    //Returns a single user via ID
    public UserResponse getUser(Long id) {

        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("USER", id));
        
        return userMapper.toDto(user);
    }

    /**
     * The function `setUserStatus` toggles the active status of a user identified by their ID.
     * 
     * @param id The `id` parameter in the `setUserStatus` method is the unique identifier of the user whose status is being toggled between active and inactive.
     */
    public void setUserStatus(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("USER", id));

        if(user.isActive()) {
            user.setActive(false);
        } else{
            user.setActive(true);
        }

        userRepository.save(user);
    }

    /**
     * The function removes a user from the repository by their ID.
     * 
     * @param id The `id` parameter is of type `Long` and represents the unique identifier of the user that needs to be removed from the system.
     */
    public void removeUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("USER", id));

        userRepository.delete(user);
    }

    public String updateUserProfile(UpdateUserProfileRequest request) {

        Long userId = getIdByEmailAndGovtId(request.govtEmail(), request.govtId());

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("USER", userId));

        boolean isUpdated = false;

        if (!Objects.equals(request.firstName(), user.getFirstName())) {
            user.setFirstName(request.firstName());
            isUpdated = true;
        }

        if (!Objects.equals(request.lastName(), user.getLastName())) {
            user.setLastName(request.lastName());
            isUpdated = true;
        }

        if (!Objects.equals(request.middleName(), user.getMiddleName())) {
            user.setMiddleName(request.middleName());
            isUpdated = true;
        }

        if (!Objects.equals(request.birthDate(), user.getBirthDate())) {
            user.setBirthDate(request.birthDate());
            isUpdated = true;
        }

        if (!Objects.equals(request.baranggay(), user.getBaranggay())) {
            user.setBaranggay(request.baranggay());
            isUpdated = true;
        }

        if (!Objects.equals(request.cityMunicipality(), user.getCityMunicpality())) {
            user.setCityMunicpality(request.cityMunicipality());
            isUpdated = true;
        }

        if (!Objects.equals(request.province(), user.getProvince())) {
            user.setProvince(request.province());
            isUpdated = true;
        }

        if (request.sex() != null) {
            Sex requestedSex = EnumUtils.parseEnum(Sex.class, request.sex(), "sex");
            if (requestedSex != user.getSex()) {
                user.setSex(requestedSex);
                isUpdated = true;
            }
        }

        if (request.role() != null) {
            Role requestedRole = EnumUtils.parseEnum(Role.class, request.role(), "role");
            if (requestedRole != user.getRole()) {
                user.setRole(requestedRole);
                isUpdated = true;
            }
        }

        if (!Objects.equals(request.govtEmail(), user.getGovtEmail())) {
            user.setGovtEmail(request.govtEmail());
            isUpdated = true;
        }

        if (!Objects.equals(request.govtId(), user.getGovtId())) {
            user.setGovtId(request.govtId());
            isUpdated = true;
        }

        if (isUpdated) {
            userRepository.save(user);
            return "Successfully updated the user's information";
        }

        return "No changes made";
    }


        //Needs to be polished.
        //The logic is clear and the exception is vague
        public Long getIdByEmailAndGovtId(String govtEmail, Long govtId) {
            User user = userRepository.findByGovtEmailAndGovtId(govtEmail, govtId).
                orElseThrow(()-> new ResourceNotFoundException("USER", govtId));

            return user.getId();
        }
}
