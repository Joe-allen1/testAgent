package com.qyl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qyl.model.entity.LayerGroupRequest;
import com.qyl.model.entity.LayerItem;
import com.qyl.service.impl.LayerGroupServiceImpl;

import java.util.List;

public interface LayerGroupService {

    boolean layerExists(String layerName);

    void createLayer(LayerItem item);

    void createLayerGroup(LayerGroupRequest req);

    void updateLayerGroupBoundingBox(String name, LayerGroupServiceImpl.BoundingBox bbox);

    Object getLayerGroup(String name);

    void deleteLayerGroup(String name);

    public void mergeIntoLayerGroup(LayerGroupRequest newRequest) throws Exception;
}
