package com.vanadis.vap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity(name = "bookmark")
public class Bookmark extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column
    private String url;
    @Column
    private String urlHost;
    @Column
    private String urlName;
    @Column
    private int tag;
    @Column
    private int urlGroup;
    @Column
    private Long userId;

    public Bookmark() {
        super();
    }

    public Bookmark(String url, String urlHost, String urlName, int tag, int urlGroup, Long userId) {
        this.url = url;
        this.urlHost = urlHost;
        this.urlName = urlName;
        this.tag = tag;
        this.urlGroup = urlGroup;
        this.userId = userId;
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

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getUrlGroup() {
        return urlGroup;
    }

    public void setUrlGroup(int urlGroup) {
        this.urlGroup = urlGroup;
    }
}