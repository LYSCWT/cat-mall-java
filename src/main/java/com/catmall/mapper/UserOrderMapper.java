package com.catmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catmall.entity.UserOrder;
import com.catmall.entity.UserRole;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrderMapper extends BaseMapper<UserOrder> {
}
