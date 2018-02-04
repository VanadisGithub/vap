package com.vanadis.vap.model;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ArticleMapper {

    @Select("SELECT * FROM article")
    List<Article> getList();

    @Insert("INSERT INTO article(title,content,article_group,tag,user_id,create_ts,update_ts) VALUES(#{title}, #{content}, #{articleGroup}, #{tag}, #{userId},#{createTs}, #{updateTs})")
    boolean insert(Article article);

    @Delete("DELETE FROM article WHERE id =#{id}")
    boolean delete(Long id);

}
