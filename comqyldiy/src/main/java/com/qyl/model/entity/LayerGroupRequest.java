package com.qyl.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class LayerGroupRequest {

    private String name;

    private List<LayerItem> items;
}
