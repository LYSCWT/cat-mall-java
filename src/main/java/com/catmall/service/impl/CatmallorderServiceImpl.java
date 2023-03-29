package com.catmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catmall.entity.Car;
import com.catmall.entity.Catmallorder;
import com.catmall.mapper.CarMapper;
import com.catmall.mapper.CatmallorderMapper;
import com.catmall.service.CarService;
import com.catmall.service.CatmallorderService;
import org.springframework.stereotype.Service;

@Service
public class CatmallorderServiceImpl extends ServiceImpl<CatmallorderMapper, Catmallorder> implements CatmallorderService {

}
