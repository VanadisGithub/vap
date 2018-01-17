package com.vanadis.mapper;

import com.vanadis.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {

    @Select("SELECT * FROM user")
    @Results({
            @Result(property = "userName",  column = "user_name"),
            @Result(property = "passWord", column = "pass_word"),
            @Result(property = "nickName", column = "nick_name"),
            @Result(property = "regTime", column = "reg_time")
    })
    List<User> getAll();

    @Select("SELECT * FROM user WHERE id = #{id}")
    @Results({
            @Result(property = "userName",  column = "user_name"),
            @Result(property = "passWord", column = "pass_word"),
            @Result(property = "nickName", column = "nick_name"),
            @Result(property = "regTime", column = "reg_time")
    })
    User getOne(Long id);

    @Insert("INSERT INTO user(user_name,pass_word,email,nick_name,reg_time) VALUES(#{userName}, #{passWord}, #{email}, #{nickName}, #{regTime})")
    void insert(User user);

    @Delete("DELETE FROM user WHERE id =#{id}")
    void delete(Long id);

}
