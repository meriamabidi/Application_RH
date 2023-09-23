package com.vermeg.back.controller;

import com.vermeg.back.entity.Conge;
import com.vermeg.back.entity.FileData;
import com.vermeg.back.entity.Role;
import com.vermeg.back.entity.User;
import com.vermeg.back.repository.FileDataRepository;
import com.vermeg.back.repository.RoleRepository;
import com.vermeg.back.service.CongeService;
import com.vermeg.back.service.RoleService;
import com.vermeg.back.service.StorageService;
import com.vermeg.back.service.UserService;
import com.vermeg.back.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Controller

public class RhController {

    private final UserService userService;

private final CongeService congeService;
private final RoleService roleService;

    @Autowired
    public RhController(UserService userService ,CongeService congeService,RoleService roleService) {
        this.userService = userService;
        this.congeService = congeService;
        this.roleService = roleService;


    }

    @GetMapping("/rhhome")

        public String rh_home() {
            return "rh_index";
        }


    @GetMapping("/add_emp")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "Add_emp";
    }


    @PostMapping("/add_emp")
    public String addUser(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        userService.saveEmp(registrationDto);
        return "redirect:/users_emp_rh";
    }

    public String getRoleName(long roleId) {
        Role role;
        role = roleService.getRoleByID(roleId);
        return role.getName();

    }

    @GetMapping("/users_emp_rh")
    public String listEMPUsers(Model model) {
        List<User> adminUsers = userService.getUsersByRoleId(Long.valueOf(5));
        model.addAttribute("getRoleName", new Function<Long, String>() {
            @Override
            public String apply(Long roleId) {
                return getRoleName(roleId);
            }
        });
        model.addAttribute("users", adminUsers);
        return "List_Emp";
    }

    @PostMapping("/user_rh/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users_emp_rh"; // Redirect back to the user management page
    }


    public String getLeaderName(long leaderId) {
        User user;
        user = userService.getUserById(leaderId);
        return user.getFirstName() +"  "+ user.getLastName();

    }
    @GetMapping("/Rh_List_conge")
    public String listAcceptedConge(Model model) {
        List<Conge> acceptedConges = congeService.getAllConge().stream()
                .filter(conge -> "accepted".equals(conge.getStatus()))
                .collect(Collectors.toList());

        model.addAttribute("conges", acceptedConges);

        model.addAttribute("getEmpName", new Function<Long, String>() {
            @Override
            public String apply(Long empId) {
                return getEmpName(empId);
            }
        });

        model.addAttribute("getLeaderName", new Function<Long, String>() {
            @Override
            public String apply(Long leaderId) {
                return getLeaderName(leaderId);
            }
        });

        return "List_conges_rh";
    }
    public String getEmpName(long empId) {
        User user;
        user = userService.getUserById(empId);
        return user.getFirstName() +"  "+ user.getLastName();

    }
    @Autowired
    private StorageService storageservice;

    @GetMapping("/req_doc_rh")
    public String showUploadForm(Model model) {
        List<FileData> files = storageservice.getAllFileData().stream()
                .filter(fileData -> fileData.getFilePath() == null)
                .collect(Collectors.toList());
        model.addAttribute("getEmpName", new Function<Long, String>() {
            @Override
            public String apply(Long empId) {
                return getEmpName(empId);
            }
        });
        model.addAttribute("files", files);
        model.addAttribute("doc", new FileData());

        return "request_doc_rh";
    }

    @PostMapping("/req_doc_rh/{id}")
    public String uploadImageToFileSystem(@RequestParam("image") MultipartFile file, @PathVariable Long id) throws IOException {

        Optional<FileData> fileDataOptional = storageservice.findById(id);

            FileData fileData = fileDataOptional.get();
            String uploadImage = storageservice.uploadImageToFileSystem(file, fileData);

        return "redirect:/req_doc_rh";
    }

    @GetMapping("/doc_rh")

    public String listDoc(Model model)
    {
        model.addAttribute("getEmpName", new Function<Long, String>() {
            @Override
            public String apply(Long empId) {
                return getEmpName(empId);
            }
        });
        List<FileData> files = storageservice.getAllFileData();
        model.addAttribute("files", files);
        return "rh_list_doc";
    }




    @GetMapping("/rh_list_doc/{fileName}")
    public ResponseEntity<byte[]> viewImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData = storageservice.downloadImageFromFileSystem(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

}
