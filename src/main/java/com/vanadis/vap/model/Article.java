package com.vanadis.vap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "blog")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column()
    private Long createTime;
    @Column()
    private Long updateTime;

    public Article() {
        super();
    }

}