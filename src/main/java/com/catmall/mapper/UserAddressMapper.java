package com.catmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catmall.entity.UserAddress;
import com.catmall.entity.UserOrder;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    //@Select("select * from user_address")
    List<UserAddress> selectUserAddressList();
}
