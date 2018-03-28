package com.vanadis.vap.model;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProxyMapper {

    @Select("SELECT * FROM proxy where error_num = 0")
    List<Proxy> getPerfectList();

    @Select("SELECT * FROM proxy where error_num < 2")
    List<Proxy> getGoodList();

    @Select("SELECT * FROM proxy")
    List<Proxy> getAll();

    @Select("SELECT * FROM proxy WHERE id = #{id}")
    Proxy getOne(Long id);

    @Select("SELECT * FROM proxy order by create_ts desc limit 1")
    Proxy getLastOne();

    @Insert("INSERT INTO proxy(ip,port,type,error_num,create_ts,update_ts) VALUES(#{ip}, #{port}, #{type}, #{errorNum}, #{createTs}, #{updateTs})")
    void insert(Proxy proxy);

    @Delete("DELETE FROM proxy WHERE id = #{id}")
    void delete(Long id);

    @Select("select 1 from proxy where ip = #{ip}")
    int isExcited(String ip);

    @Update("update proxy set error_num = error_num + 1 , update_ts = #{updateTs} where ip = #{ip}")
    void addErrorNum(String ip, Long updateTs);

}
