package com.vermeg.back.repository;

import com.vermeg.back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
User findById(long id);

    List<User> findByRoleId(Long roleId);





}