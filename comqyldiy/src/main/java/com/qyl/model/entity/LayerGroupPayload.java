package com.qyl.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LayerGroupPayload {

    @JsonProperty("layerGroup")
    private LayerGroup layerGroup;


    @Data
    public static class LayerGroup {
        private String name;

        private Layers layers;

        private Styles styles;
    }

    @Data
    public static class Layers {
        @JsonProperty("layer")
        private List<String> layer;
    }

    @Data
    public static class Styles {
        @JsonProperty("style")
        private List<String> style;
    }
}
