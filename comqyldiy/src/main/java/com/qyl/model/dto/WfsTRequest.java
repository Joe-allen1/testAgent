package com.qyl.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class WfsTRequest {
    private String layerName;
    private List<FeatureData> features;
}
