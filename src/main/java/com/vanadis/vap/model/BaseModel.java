package com.vanadis.vap.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

//添加基类支持
@MappedSuperclass
public class BaseModel {

    @Id
    @GeneratedValue
    private Long id;
    @Column()
    public Long createTs;
    @Column()
    public Long updateTs;

    public BaseModel() {
        this.createTs = this.updateTs = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getcreateTs() {
        return createTs;
    }

    public void setcreateTs(Long createTs) {
        this.createTs = createTs;
    }

    public Long getupdateTs() {
        return updateTs;
    }

    public void setupdateTs(Long updateTs) {
        this.updateTs = updateTs;
    }
}
