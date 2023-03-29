package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@Data
public class RoleName implements Serializable {

    @NotBlank(message = "角色名称不能为空")
    private String name;

}
