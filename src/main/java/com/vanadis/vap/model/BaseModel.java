package com.vanadis.vap.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.text.SimpleDateFormat;
import java.util.Date;

//添加基类支持
@MappedSuperclass
public class BaseModel {

    public static SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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

    public Long getCreateTs() {
        return createTs;
    }

    public String getCreateTsStr() {
        Date date = new Date(createTs);
        return ymdhm.format(date);
    }

    public void setCreateTs(Long createTs) {
        this.createTs = createTs;
    }

    public Long getUpdateTs() {
        return updateTs;
    }

    public String getUpdateTsStr() {
        Date date = new Date(updateTs);
        return ymdhm.format(date);
    }

    public void setUpdateTs(Long updateTs) {
        this.updateTs = updateTs;
    }
}
