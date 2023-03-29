package com.catmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catmall.entity.UserCar;
import com.catmall.mapper.UserCarMapper;
import com.catmall.service.UserCarService;
import org.springframework.stereotype.Service;

@Service
public class UserCarServiceImpl extends ServiceImpl<UserCarMapper, UserCar> implements UserCarService {
}
