package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Data
public class UserCommon implements Serializable {
    private long id;
    private String name;
    private String avatar;
    private String email;
    private String phone;
    private String sex;
    private LocalDateTime birthday;
    private String roleName;
}
