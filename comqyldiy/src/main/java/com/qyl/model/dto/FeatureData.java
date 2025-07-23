package com.qyl.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FeatureData {
    private String geometryType;  // "Point", "LineString", "Polygon"

    private double[] coordinates; // 点坐标 [x,y] 或线/面的坐标数组

    private Map<String, Object> attributes; // 属性键值对
}
