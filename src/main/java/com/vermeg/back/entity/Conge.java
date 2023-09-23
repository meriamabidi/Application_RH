package com.vermeg.back.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="conge" )
        public class Conge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date_d ;
    private String date_f;
    private String type;
    private String status;

    private long idEmp;
    private long idLeader;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate_d() {
        return date_d;
    }

    public void setDate_d(String date_d) {
        this.date_d = date_d;
    }

    public String getDate_f() {
        return date_f;
    }

    public void setDate_f(String date_f) {
        this.date_f = date_f;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public long getId_emp() {
        return idEmp;
    }

    public void setId_emp(long id_emp) {
        this.idEmp = id_emp;
    }

    public long getId_leader() {
        return idLeader;
    }

    public void setId_leader(long id_leader) {
        this.idLeader = id_leader;
    }

}
