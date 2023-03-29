package com.catmall.common.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Data
@Setter
public class CarData implements Serializable {

    private Long id;
    private int count;
    private String serial;
    private int userId;
    private String imgUrl;
    private String name;
    private double price;


}
