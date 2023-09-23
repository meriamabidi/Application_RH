package com.vermeg.back.service;


import com.vermeg.back.entity.Conge;
import com.vermeg.back.entity.User;
import com.vermeg.back.repository.CongeRepository;
import com.vermeg.back.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CongeService {
    static CongeRepository congeRepository;
   @Autowired
   public CongeService(CongeRepository congeRepository) {
       this.congeRepository = congeRepository;
   }

    public List<Conge> getAllConge() {
        return congeRepository.findAll();
    }

    public Conge reject(Long id) {
       Conge conge = congeRepository.findById(id).orElse(null);
       if(conge!=null)
       {
           conge.setStatus("rejected");
           return congeRepository.save(conge);
       }
        return null;
    }
    public static Conge save(Conge conge) {
        return congeRepository.save(conge);
    }

    public Conge accept(Long id) {
        Conge conge = congeRepository.findById(id).orElse(null);
        if(conge!=null)
        {
            conge.setStatus("accepted");
            return congeRepository.save(conge);
        }
        return null;
    }
    public List<Conge> getAllCongeByEmpId(Long empId) {
        return congeRepository.findByIdEmp(empId);
    }


}
