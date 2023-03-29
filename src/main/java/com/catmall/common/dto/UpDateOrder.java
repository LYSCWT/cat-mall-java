package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Data
public class UpDateOrder implements Serializable {
    private long id;
    private int statu;
}
