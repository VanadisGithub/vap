package com.vanadis.vap.model;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProxyMapper {

    @Select("SELECT count(*) FROM proxy where error_num < 0 and status = 0")
    int getPerfectProxyNum();

    @Select("SELECT count(*) FROM proxy where error_num < #{num} and status = 0")
    int getGoodProxyNum(int num);

    @Select("select count(*) from proxy where ip = #{ip}")
    int isExcited(String ip);

    @Select("select error_num from proxy group by error_num order by count(*) desc limit 1")
    int getMostErrorNum();

    @Select("SELECT * FROM proxy where error_num < 0 and status = 0")
    List<Proxy> getPerfectList();

    @Select("SELECT * FROM proxy where error_num < #{num} and status = 0")
    List<Proxy> getGoodList(int num);

    @Select("SELECT * FROM proxy")
    List<Proxy> getAll();

    @Update("UPDATE proxy set error_num = #{errorNum} where error_num < #{errorNum}")
    boolean updateErrorNum(int errorNum);

    @Update("update proxy set error_num = error_num + 1 , update_ts = #{1} where ip = #{0}")
    boolean addErrorNum(String ip, Long updateTs);

    @Update("update proxy set error_num = error_num - 1 , update_ts = #{1} where ip = #{0}")
    boolean subErrorNum(String ip, Long updateTs);

    @Update("update proxy set status = 1 , update_ts = #{1} where ip = #{0}")
    boolean updateStatus(String ip, Long updateTs);

    @Update("update proxy set ip = #{0} where id = #{1}")
    boolean updateIp(String ip, Long id);

    @Insert("INSERT INTO proxy(ip,port,type,error_num,status,create_ts,update_ts) VALUES(#{ip}, #{port}, #{type}, #{errorNum}, #{status}, #{createTs}, #{updateTs})")
    boolean insert(Proxy proxy);

    @Delete("DELETE FROM proxy WHERE id = #{id}")
    void delete(Long id);

    @Delete("DELETE FROM proxy WHERE error_num >= #{errorNum}")
    void deleteErrorNum(int errorNum);

}
