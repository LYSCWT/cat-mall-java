package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Data
public class UpdateUserById implements Serializable {
    private long id;
    private String name;
    private String sex;
    private LocalDateTime birthday;
}
