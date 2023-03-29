package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Data
public class UpdateAddress implements Serializable {
    private long id;
    private String people;
    private String phone;
    private String address;
}
