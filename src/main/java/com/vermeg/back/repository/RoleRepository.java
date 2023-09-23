package com.vermeg.back.repository;


import com.vermeg.back.entity.Role;
import com.vermeg.back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
Role findById(long id);

}