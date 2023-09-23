package com.vermeg.back.controller;


import com.vermeg.back.entity.Conge;
import com.vermeg.back.entity.User;
import com.vermeg.back.service.AuthenticationFacade;
import com.vermeg.back.service.CongeService;
import com.vermeg.back.service.RoleService;
import com.vermeg.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller

public class LeaderController {

    private final UserService userService;

    private final CongeService congeService;
    private final RoleService roleService;
    @Autowired
    public LeaderController(UserService userService ,CongeService congeService,RoleService roleService) {
        this.userService = userService;
        this.congeService = congeService;
        this.roleService = roleService;

    }

    @GetMapping("/leaderhome")

    public String leader_home() {
        return "leader_index";
    }

    public String getEmpName(long empId) {
        User user;
        user = userService.getUserById(empId);
        return user.getFirstName() +"  "+ user.getLastName();

    }


    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("/leader_conge_req")
    public String listCongeReq(Model model) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String loggedInUsername = authentication.getName();

        User loggedInUser = userService.getUserByEmail(loggedInUsername);

        List<Conge> filteredConges = congeService.getAllConge().stream()
                .filter(conge -> conge.getStatus() == null && conge.getId_leader() == loggedInUser.getId())
                .collect(Collectors.toList());

        model.addAttribute("conges", filteredConges);

        model.addAttribute("getEmpName", new Function<Long, String>() {
            @Override
            public String apply(Long empId) {
                return getEmpName(empId);
            }
        });

        return "conge_traite";
    }

    @GetMapping("/leader_conge")
    public String listConge(@RequestParam(value = "status", required = false) String status, Model model) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String loggedInUsername = authentication.getName();

        User loggedInUser = userService.getUserByEmail(loggedInUsername);

        List<Conge> filteredConges = congeService.getAllConge().stream()
                .filter(conge -> {
                    boolean isLeaderConge = conge.getId_leader() == loggedInUser.getId();
                    if ("accepted".equals(status)) {
                        return isLeaderConge && "accepted".equals(conge.getStatus());
                    } else if ("rejected".equals(status)) {
                        return isLeaderConge && "rejected".equals(conge.getStatus());
                    }
                    return isLeaderConge;
                })
                .collect(Collectors.toList());

        model.addAttribute("conges", filteredConges);
        model.addAttribute("getEmpName", new Function<Long, String>() {
            @Override
            public String apply(Long empId) {
                return getEmpName(empId);
            }
        });

        return "List_conge_leader";
    }


    @PostMapping(value = "/accept/{id}", consumes = "application/x-www-form-urlencoded")
    public String accept(@PathVariable Long id) {
        congeService.accept(id);
        return "redirect:/leader_conge_req";
    }

    @PostMapping(value = "/reject/{id}", consumes = "application/x-www-form-urlencoded")
    public String reject(@PathVariable Long id) {
        congeService.reject(id);
        return "redirect:/leader_conge_req";
    }



}
