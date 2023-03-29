package com.catmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catmall.entity.UserAddress;
import com.catmall.entity.UserCar;
import com.catmall.mapper.UserAddressMapper;
import com.catmall.mapper.UserCarMapper;
import com.catmall.service.UserAddressService;
import com.catmall.service.UserCarService;
import org.springframework.stereotype.Service;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {
}
