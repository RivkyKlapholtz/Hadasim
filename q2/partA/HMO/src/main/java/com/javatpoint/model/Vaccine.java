package com.javatpoint.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ManufactureType manufacturer;

    public Vaccine() {
    }

    public enum ManufactureType {
        Pfizer,
        modern,
        AstraZeneca,
        Novavax
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ManufactureType getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufactureType manufacturer) {
        this.manufacturer = manufacturer;
    }
}
