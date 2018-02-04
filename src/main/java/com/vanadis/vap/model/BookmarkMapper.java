package com.vanadis.vap.model;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BookmarkMapper {

    @Select("SELECT * FROM bookmark WHERE user_id = #{userId}")
    List<Bookmark> getList(Long userId);

    @Insert("INSERT INTO bookmark(url,url_host,url_name,tag,url_group,user_id,create_ts,update_ts) VALUES(#{url}, #{urlHost}, #{urlName}, #{tag}, #{urlGroup}, #{userId},#{createTs}, #{updateTs})")
    boolean insert(Bookmark bookmark);

    @Delete("DELETE FROM bookmark WHERE id =#{id}")
    boolean delete(Long id);

}
