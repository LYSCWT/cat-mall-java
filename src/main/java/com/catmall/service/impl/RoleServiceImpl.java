package com.catmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catmall.entity.Role;
import com.catmall.mapper.RoleMapper;
import com.catmall.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
