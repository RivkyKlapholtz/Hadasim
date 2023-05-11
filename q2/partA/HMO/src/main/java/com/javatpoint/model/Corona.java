package com.javatpoint.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table
public class Corona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Date sickDate;

    @Column(nullable = false)
    private Date recoveryDate;

    public Corona() {
    }

    public Date getSickDate() {
        return sickDate;
    }

    public void setSickDate(Date sickDate) {
        this.sickDate = sickDate;
    }

    public Date getRecoveryDate() {
        return recoveryDate;
    }

    public void setRecoveryDate(Date recoveryDate) {
        this.recoveryDate = recoveryDate;
    }
}