package com.vermeg.back.controller;


import com.vermeg.back.entity.Role;
import com.vermeg.back.entity.User;
import com.vermeg.back.service.RoleService;
import com.vermeg.back.service.UserService;
import com.vermeg.back.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

@Controller

public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/adminhome")
    public String admin_home() {
        return "admin_index";
    }


    public String getRoleName(long roleId) {
 Role role;
role = roleService.getRoleByID(roleId);
return role.getName();
    }

    @GetMapping("/users")
    public String listUsers(Model model, @RequestParam(required = false) String query) {
        List<User> usersList;

        if (query != null && !query.isEmpty()) {

            usersList = userService.searchUsers(query);
        } else {

            usersList = userService.getAllUsers();
        }

        model.addAttribute("users", usersList);

        model.addAttribute("getRoleName", new Function<Long, String>() {
            @Override
            public String apply(Long roleId) {
                return getRoleName(roleId);
            }
        });

        return "List_Users";
    }
    @GetMapping("/list_users")
    public String listUsersByRole(@RequestParam("roleId") Long roleId, Model model) {
        List<User> users = userService.getUsersByRoleId(roleId);
        model.addAttribute("getRoleName", new Function<Long, String>() {
            @Override
            public String apply(Long roleId) {
                return getRoleName(roleId);
            }
        });
        model.addAttribute("users", users);
        return "List_Users";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        List<Role> allRoles = roleService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "Update_User";
    }
    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User updatedUser, @RequestParam("roleIds") Long roleIds) {
        userService.updateUserWithRoles(id,updatedUser.getEmail(), updatedUser.getFirstName(), updatedUser.getLastName(), roleIds);
        return "redirect:/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users"; // Redirect back to the user management page
    }



    @GetMapping("/add_user")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "Add_User";
    }
    @PostMapping("/add_user")
    public String addUser(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        userService.save(registrationDto);
        return "redirect:/users";
    }




}
