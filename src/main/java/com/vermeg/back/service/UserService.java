package com.vermeg.back.service;

import com.vermeg.back.entity.User;
import com.vermeg.back.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);


    User saveEmp(UserRegistrationDto registrationDto);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(User user);


    User updateUserWithRoles(Long userId,String email, String firstName, String lastName, long role);

    void deleteUser(Long id);


    List<User> getUsersByRoleId(Long roleId);

    User getUserByEmail(String email);

    List<User> searchUsers(String query);
}
