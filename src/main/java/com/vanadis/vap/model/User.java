package com.vanadis.vap.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "user")
public class User extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    public Long userId;
    @Column
    private String userName;
    @Column
    private String passWord;
    @Column
    private String email;
    @Column
    private String nickName;
    @Column
    private Long registerTime;

    public User() {
        super();
    }

    public User(String userName, String passWord, String email, String nickName, Long registerTime) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.nickName = nickName;
        this.registerTime = registerTime;
        this.createTs = this.updateTs = System.currentTimeMillis();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
    }
}