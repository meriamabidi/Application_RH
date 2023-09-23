package com.vermeg.back.service;


import com.vermeg.back.entity.Role;
import com.vermeg.back.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleByID(long role) {
        return roleRepository.findById(role);
    }

    // Add this new method to get a Role by name

}

