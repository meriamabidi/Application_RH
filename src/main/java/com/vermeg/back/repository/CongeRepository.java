package com.vermeg.back.repository;


import com.vermeg.back.entity.Conge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CongeRepository extends JpaRepository<Conge, Long> {


    List<Conge> findByIdEmp(Long idEmp);

}
