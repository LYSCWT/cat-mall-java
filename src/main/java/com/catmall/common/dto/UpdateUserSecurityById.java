package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Data
public class UpdateUserSecurityById implements Serializable {
    private long id;
    private String phone;
    private String email;
}
