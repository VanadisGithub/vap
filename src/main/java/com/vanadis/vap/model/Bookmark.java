package com.vanadis.vap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "user")
public class Bookmark extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    public Long id;
    @Column
    private String url;
    @Column
    private String tag;
    @Column
    private String group;
    @Column
    public Long userId;

    public Bookmark() {
        super();
    }

    public Bookmark(String url, String tag, String group, Long userId) {
        this.url = url;
        this.tag = tag;
        this.group = group;
        this.userId = userId;
        this.createTs = this.updateTs = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}