package com.catmall.controller;

import com.catmall.mapper.*;
import com.catmall.service.*;
import com.catmall.utils.JwtUtils;
import com.catmall.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	CatmallorderMapper catmallorderMapper;

	@Autowired
	CarMapper carMapper;

	@Autowired
	CarService carService;

	@Autowired
	UserCarMapper userCarMapper;

	@Autowired
	UserCarService userCarService;

	@Autowired
	UserOrderService userOrderService;

	@Autowired
	UserAddressService userAddressService;

	@Autowired
	AddressService addressService;

	@Autowired
	UserAddressMapper userAddressMapper;

	@Autowired
	AddressMapper addressMapper;

	@Autowired
	ThingsMapper thingsMapper;

	@Autowired
	FoodMapper foodMapper;

	@Autowired
	AnimalMapper animalMapper;

	@Autowired
	RoleMapper roleMapper;

	@Autowired
	UserRoleMapper userRoleMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	HttpServletRequest req;

	@Autowired
	RedisUtil redisUtil;

	@Autowired
	UserService userService;

	@Autowired
	JwtUtils jwtUtils;


}
