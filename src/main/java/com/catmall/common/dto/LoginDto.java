package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Data
public class LoginDto implements Serializable {

    private String password;

    private String name;
}
