package com.catmall.mapper;

import com.catmall.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catmall.entity.UserAddress;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper extends BaseMapper<User> {
    @Select("UPDATE user SET password=? WHERE (name = ?)")
    int updatePassword(String password,String name);

}
