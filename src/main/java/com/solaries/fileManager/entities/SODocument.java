package com.solaries.fileManager.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Table
public class SODocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String filePath ;
    private String name;
    private LocalDateTime createdAt;
    private Long size;



    public String getFilePath () {
        return filePath;
    }

    public SODocument setFilePath (String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getName () {
        return name;
    }

    public SODocument setName (String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getCreatedAt () {
        return createdAt;
    }

    public SODocument setCreatedAt (LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Long getSize () {
        return size;
    }

    public SODocument setSize (Long size) {
        this.size = size;
        return this;
    }


    public SODocument setId (Long id) {
        this.id = id;
        return this;
    }

    @Id
    public Long getId () {
        return id;
    }
}