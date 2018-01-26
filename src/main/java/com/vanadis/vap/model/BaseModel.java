package com.vanadis.vap.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

//添加基类支持
@MappedSuperclass
public class BaseModel {

    @Column()
    public Long createTs;
    @Column()
    public Long updateTs;

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
