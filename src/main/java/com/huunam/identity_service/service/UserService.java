package com.huunam.identity_service.service;

import com.huunam.identity_service.dto.request.UserCreationRequest;
import com.huunam.identity_service.dto.request.UserUpdateRequest;
import com.huunam.identity_service.dto.response.UserResponse;
import com.huunam.identity_service.entity.User;
import com.huunam.identity_service.enums.Role;
import com.huunam.identity_service.exception.AppException;
import com.huunam.identity_service.exception.ErrorCode;
import com.huunam.identity_service.mapper.UserMapper;
import com.huunam.identity_service.repository.RoleRepository;
import com.huunam.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // field define mac dinh la private final neu khong khai
                                                              // bao gi
@Slf4j // inject Logger
public class UserService {

    UserRepository userRepository; // private final UserRepository userRepository
    RoleRepository roleRepository; // private final RoleRepository roleRepository
    UserMapper userMapper; // private final UserMapper userMapper
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) { // request la info can thiet de tao table user, muon
                                                                  // nhu v thi phai tao dto
        if (userRepository.existsByUsername(request.getUsername())) // check user exist
            throw new AppException(ErrorCode.USER_EXISTED);// globalExceptionHandler se tu dong lay ex va xu ly

        User user = userMapper.toUser(request); // map request into User
        // instead of below code
        // user.setUsername(request.getUsername());
        // user.setPassword(request.getPassword());
        // user.setFirstName(request.getFirstName());
        // user.setLastName(request.getLastName());
        // user.setDob(request.getDob());

        // PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); bo di vi da
        // su dung bean passwordEncoder da khai bao
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // set them role mac dinh cua user khi ma onboard la role user de verify
        HashSet<String> roles = new HashSet<>();// use HashSet
        roles.add(Role.USER.name());// add role default is USER
        // user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user)); // persist down to database
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request); // instead of below code
        // user.setPassword(request.getPassword());
        // user.setFirstName(request.getFirstName());
        // user.setLastName(request.getLastName());
        // user.setDob(request.getDob());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles()); // get all roles
        user.setRoles(new HashSet<>(roles)); // convert List to Set (HashSet)

        return userMapper.toUserResponse(userRepository.save(user));

    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    // Co Role Admin thi moi duoc truy cap vao
    @PreAuthorize("hasRole('ADMIN')") // PreAuthorize: spring se tao ra mot process trc getAll function
    // muon vao duoc getAll thi preAuthor se kiem tra truoc khi goi ham phai co role
    // admin
    public List<UserResponse> getAllUsers() {
        log.info("Get in method get Users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name") // inject sau khi method duoc thuc hien xong, neu
                                                                   // thoa dk thi method moi return result ve
    public UserResponse getUser(String id) {
        log.info("Get in method get user by Id");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

}
