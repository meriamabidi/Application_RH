package com.vermeg.back.controller;


import com.vermeg.back.entity.Conge;
import com.vermeg.back.entity.FileData;
import com.vermeg.back.entity.User;
import com.vermeg.back.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;


@Controller
public class EmployeController {

    private final UserService userService;

    private final CongeService congeService;
    private final RoleService roleService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    public EmployeController(UserService userService ,CongeService congeService,RoleService roleService) {
        this.userService = userService;
        this.congeService = congeService;
        this.roleService = roleService;

    }
    @GetMapping("/employehome")
    public String home() {
        return "employe_index";
    }

    @GetMapping("/demande_conge")
    public String demandeConge(Model model) {
        Conge conge = new Conge();
        List<User> leaderUsers = userService.getUsersByRoleId(Long.valueOf(4));
        model.addAttribute("leaderUsers", leaderUsers);
        model.addAttribute("Conge", conge);

        return "demande_conge";
    }

    @PostMapping("/demande_conge")
    public String submitDemandeConge(@ModelAttribute("Conge") Conge conge,
                                     HttpSession session) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String loggedInUsername = authentication.getName();
        User loggedInUser = userService.getUserByEmail(loggedInUsername);
        conge.setId_emp(loggedInUser.getId());

        CongeService.save(conge);

        return "redirect:/list_conge";
    }

    public String getEmpName(long empId) {
        User user;
        user = userService.getUserById(empId);
        return user.getFirstName() +"  "+ user.getLastName();

    }

    @GetMapping("/list_conge")
    public String listConge(Model model) {

        Authentication authentication = authenticationFacade.getAuthentication();
        String loggedInUsername = authentication.getName();

        User loggedInUser = userService.getUserByEmail(loggedInUsername);
        List<Conge> congesForLoggedInUser = congeService.getAllCongeByEmpId(loggedInUser.getId());

        model.addAttribute("conges", congesForLoggedInUser);

        model.addAttribute("getEmpName", new Function<Long, String>() {
            @Override
            public String apply(Long empId) {
                return getEmpName(empId);
            }
        });
        return "list_conge";
    }


    @GetMapping("/demande_doc_emp")
    public String demande_doc_emp(Model model) {
        FileData file = new FileData();
        model.addAttribute("Doc", file);

        return "demande_doc";
    }

    @Autowired
    private StorageService storageservice;

    @PostMapping("/demande_doc_emp")
    public String demande_doc(@ModelAttribute("Doc") FileData doc,
                                     HttpSession session) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String loggedInUsername = authentication.getName();
        User loggedInUser = userService.getUserByEmail(loggedInUsername);
        doc.setId_emp(loggedInUser.getId());

        storageservice.save(doc);

        return "redirect:/list_doc_emp";
    }
    @GetMapping("/list_doc_emp")
    public String listDoc(Model model) {

        Authentication authentication = authenticationFacade.getAuthentication();
        String loggedInUsername = authentication.getName();

        User loggedInUser = userService.getUserByEmail(loggedInUsername);
        List<FileData> filesForLoggedInUser = storageservice.getAllDocByEmpId(loggedInUser.getId());

        model.addAttribute("files", filesForLoggedInUser);

        return "list_doc";
    }
   @GetMapping("/list_doc_emp/{fileName}")
    public ResponseEntity<byte[]> downloadImageFromFileSystem(@PathVariable String fileName ) throws IOException {

        byte[] imageData = storageservice.downloadImageFromFileSystem(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }




}
