package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Data
public class IdSelect implements Serializable {
    private int[] id;
}

