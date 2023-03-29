package com.catmall.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Data
public class InsertOrder implements Serializable {
    private int id;
    private int addressid;
    private String name;
    private int num;
    private double total;
}
