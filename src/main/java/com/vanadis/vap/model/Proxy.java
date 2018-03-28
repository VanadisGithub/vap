package com.vanadis.vap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity(name = "proxy")
public class Proxy extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column
    private String ip;
    @Column
    private String port;
    @Column
    private int type;
    @Column
    private int errorNum;

    public Proxy() {
    }

    public Proxy(String ip, String port, int type, int errorNum) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.errorNum = errorNum;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }
}