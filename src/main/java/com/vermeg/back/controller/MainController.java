package com.vermeg.back.controller;


import com.vermeg.back.entity.Role;
import com.vermeg.back.entity.User;
import com.vermeg.back.service.AuthenticationFacade;
import com.vermeg.back.service.CongeService;
import com.vermeg.back.service.RoleService;
import com.vermeg.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    private final UserService userService;
    @Autowired
    private final RoleService roleService;
    @Autowired
    public MainController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

    }
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        }
        return "a_login";
    }

    @GetMapping("/")
    public String home() {
        return "pages-login";
    }


    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String loggedInUsername = authentication.getName();
        User user = userService.getUserByEmail(loggedInUsername);
        String roleName = roleService.getRoleByID(user.getRoleId()).getName();

            model.addAttribute("user", user);
         model.addAttribute("roleName", roleName);

        return "user_profile";
    }



    @PostMapping("/profile")
    public String updateprofile( @ModelAttribute User updatedUser) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String loggedInUsername = authentication.getName();
        User user = userService.getUserByEmail(loggedInUsername);

        userService.updateUserWithRoles(user.getId(),updatedUser.getEmail(), updatedUser.getFirstName(), updatedUser.getLastName(), user.getRoleId());
        return "redirect:/profile";
    }


  /*  @GetMapping("/check-session")
    public String checkSession(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String userId = authentication.getName();
            model.addAttribute("userId", userId);
        } else {
            model.addAttribute("userId", "User not authenticated");
        }

        return "active";
    }*/
}
