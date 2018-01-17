package com.vanadis.model;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> getAll();

    @Select("SELECT * FROM user WHERE user_id = #{userId}")
    User getOne(Long id);

    @Select("SELECT * FROM user order by create_ts desc limit 1")
    User getLastOne();

    @Insert("INSERT INTO user(user_name,pass_word,email,nick_name,register_time,create_ts,update_ts) VALUES(#{userName}, #{passWord}, #{email}, #{nickName}, #{registerTime},#{createTs}, #{updateTs})")
    void insert(User user);

    @Delete("DELETE FROM user WHERE user_id =#{userId}")
    void delete(Long id);

}
