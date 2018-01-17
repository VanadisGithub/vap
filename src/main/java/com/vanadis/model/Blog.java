package com.vanadis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "blog")
public class Blog implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String title;
    @Column(unique = true)
    private String content;
    @Column(nullable = true, unique = true)
    private String email;
    @Column(nullable = true, unique = true)
    private int clickNum;
    private String regTime;

    public Blog() {
        super();
    }



}