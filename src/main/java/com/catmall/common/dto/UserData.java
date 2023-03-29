package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class UserData implements Serializable {
    private long id;
    private String username;
    private LocalDateTime created;
    private Integer statu;
    private String role;
}
