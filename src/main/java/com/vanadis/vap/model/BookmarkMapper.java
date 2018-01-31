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

    @Insert("INSERT INTO bookmark(url,tag,group,user_id,create_ts,update_ts) VALUES(#{url}, #{tag}, #{userId},#{createTs}, #{updateTs})")
    boolean insert(Bookmark bookmark);

    @Delete("DELETE FROM bookmark WHERE user_id =#{userId}")
    boolean delete(Long id);

}
