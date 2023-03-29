package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Data
public class SelectUserName implements Serializable {
    @NotBlank(message = "用户名不能为空")
    private String name;
}
