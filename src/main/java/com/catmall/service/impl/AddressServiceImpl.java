package com.catmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catmall.entity.Address;
import com.catmall.entity.Animal;
import com.catmall.mapper.AddressMapper;
import com.catmall.mapper.AnimalMapper;
import com.catmall.service.AddressService;
import com.catmall.service.AnimalService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

}
