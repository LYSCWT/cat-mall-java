package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Data
public class InsertUserInfo implements Serializable {
    private String name;
    private String password;
    private String phone;
}
