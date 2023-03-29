package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Data
public class UserInfoById implements Serializable {

    private long id;

    private String username;

    private String avatar;

    private String email;

    private String city;

    private LocalDateTime lastLogin;
}
