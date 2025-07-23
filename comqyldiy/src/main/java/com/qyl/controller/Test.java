package com.qyl.controller;

import com.qyl.model.entity.LayerGroupPayload;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/layer-group")
public class Test {


//    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        String geoserverUrl = "http://localhost:8080/geoserver";
//        String workspace = "ne";
//        LayerGroupRequest request = new LayerGroupRequest();
//
//        request.setName("qyl");
//        LayerItem item1 = new LayerItem();
//        item1.setLayer("ne:populated_places");
//        item1.setStyle("point");
//        LayerItem item2 = new LayerItem();
//        item2.setLayer("ne:coastlines");
//        item2.setStyle("line");
//        ArrayList<LayerItem> list = new ArrayList<>();
//        list.add(item1);
//        list.add(item2);
//        request.setItems(list);
//
//        String url = String.format("%s/rest/workspaces/%s/layergroups", geoserverUrl, workspace);
//
//
//        List<String> layers = request.getItems().stream().map(LayerItem::getLayer).collect(Collectors.toList());
//        List<String> styles = request.getItems().stream().map(LayerItem::getStyle).collect(Collectors.toList());
//
//        Map<String, Object> layerGroup = new HashMap<>();
//        layerGroup.put("name", request.getName());
//        layerGroup.put("layers", layers);
//        layerGroup.put("styles", styles);
//
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("layerGroup", layerGroup);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBasicAuth("admin", "geoserver");
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
//
//        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//    }




    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        String geoserverUrl = "http://localhost:8080/geoserver";
        String workspace = "ne";
        String url = String.format("%s/rest/workspaces/%s/layergroups", geoserverUrl, workspace);

        // 构建你的请求数据
//        LayerGroupRequest request = new LayerGroupRequest();
//        request.setName("qyl");
//
//        LayerItem item1 = new LayerItem();
//        item1.setLayer("ne:populated_places");
//        item1.setStyle("point");
//        LayerItem item2 = new LayerItem();
//        item2.setLayer("ne:coastlines");
//        item2.setStyle("line");
//        ArrayList<LayerItem> list = new ArrayList<>();
//        list.add(item1);
//        list.add(item2);
//
//
//        request.setItems(list);

        // 转为标准 GeoServer payload


        LayerGroupPayload payload = new LayerGroupPayload();

        LayerGroupPayload.LayerGroup group = new LayerGroupPayload.LayerGroup();
        group.setName("qyl");

        // 设置图层
        LayerGroupPayload.Layers layers = new LayerGroupPayload.Layers();
        layers.setLayer(Arrays.asList("ne:populated_places", "ne:coastlines"));
        group.setLayers(layers);

        // 设置样式
        LayerGroupPayload.Styles styles = new LayerGroupPayload.Styles();
        styles.setStyle(Arrays.asList("point", "line"));
        group.setStyles(styles);

        // 最外层包裹
        payload.setLayerGroup(group);




        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "geoserver");
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> mediaTypeArrayList = new ArrayList<MediaType>();
        mediaTypeArrayList.add(MediaType.APPLICATION_JSON);
        headers.setAccept(mediaTypeArrayList);

        HttpEntity<LayerGroupPayload> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
    }



}

