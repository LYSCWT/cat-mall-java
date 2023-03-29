package com.catmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catmall.entity.Car;
import com.catmall.mapper.CarMapper;
import com.catmall.service.CarService;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {

}
