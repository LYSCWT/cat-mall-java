package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Data
public class UpdateCarById implements Serializable {
    private int id;
    private String serial;
    private int count;
    private int userId;
}
