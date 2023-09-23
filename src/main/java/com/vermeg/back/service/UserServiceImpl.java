package com.vermeg.back.service;

import com.vermeg.back.entity.Role;
import com.vermeg.back.entity.User;
import com.vermeg.back.repository.RoleRepository;
import com.vermeg.back.repository.UserRepository;
import com.vermeg.back.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RoleRepository roleRepository;

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;



    }

    @Override
    public User save(UserRegistrationDto registrationDto) {

        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()) , 1);

        return userRepository.save(user);
    }

    @Override
    public User saveEmp(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()) , 5);

        return userRepository.save(user);
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }


    @Override
    public User updateUserWithRoles(Long userId,String email, String firstName, String lastName, long role) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRoleId(role);
            return userRepository.save(user);
        }
        return null;
    }


    @Autowired
    private RoleService roleService;


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }



  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepository.findByEmail(username);
      if (user == null) {
          throw new UsernameNotFoundException("Invalid username or password.");
      }
      List<GrantedAuthority> authorities = new ArrayList<>();
      Role userRole = roleRepository.findById(user.getRoleId());
      authorities.add(new SimpleGrantedAuthority(userRole.getName()));
      return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
  }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }



    @Override
    public List<User> getUsersByRoleId(Long roleId) {
        return userRepository.findByRoleId(roleId);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> searchUsers(String query) {
        List<User> allUsers = userRepository.findAll(); // Replace userRepository with your actual repository

        List<User> matchingUsers = new ArrayList<>();

        for (User user : allUsers) {
            if (user.getFirstName().toLowerCase().contains(query.toLowerCase())
                    || user.getLastName().toLowerCase().contains(query.toLowerCase())
                    || user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                matchingUsers.add(user);
            }
        }

        return matchingUsers;
    }

}
