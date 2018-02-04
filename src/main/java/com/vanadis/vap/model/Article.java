package com.vanadis.vap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "article")
    public class Article extends BaseModel implements Serializable {

    @Column
    private String title;
    @Column
    private String content;
    @Column
    private Long userId;
    @Column
    private int articleGroup;
    @Column
    private int tag;
    @Column
    private int clickNum;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getArticleGroup() {
        return articleGroup;
    }

    public void setArticleGroup(int articleGroup) {
        this.articleGroup = articleGroup;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }
}