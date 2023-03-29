package com.catmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catmall.entity.UserOrder;
import com.catmall.entity.UserRole;
import com.catmall.mapper.UserOrderMapper;
import com.catmall.mapper.UserRoleMapper;
import com.catmall.service.UserOrderService;
import com.catmall.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOrderService {
}
