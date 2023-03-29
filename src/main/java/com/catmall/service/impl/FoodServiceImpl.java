package com.catmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catmall.entity.Food;
import com.catmall.entity.Role;
import com.catmall.mapper.FoodMapper;
import com.catmall.mapper.RoleMapper;
import com.catmall.service.FoodService;
import com.catmall.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

}
