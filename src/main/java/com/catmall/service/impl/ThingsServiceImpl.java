package com.catmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catmall.entity.Role;
import com.catmall.entity.Things;
import com.catmall.mapper.RoleMapper;
import com.catmall.mapper.ThingsMapper;
import com.catmall.service.RoleService;
import com.catmall.service.ThingsService;
import org.springframework.stereotype.Service;

@Service
public class ThingsServiceImpl extends ServiceImpl<ThingsMapper, Things> implements ThingsService {

}
