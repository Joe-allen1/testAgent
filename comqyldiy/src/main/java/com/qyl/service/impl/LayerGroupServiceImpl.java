package com.qyl.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qyl.model.entity.LayerGroupPayload;
import com.qyl.model.entity.LayerGroupRequest;
import com.qyl.model.entity.LayerItem;
import com.qyl.service.LayerGroupService;
import lombok.Data;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LayerGroupServiceImpl implements LayerGroupService {

    private final String geoserverUrl = "http://localhost:8080/geoserver";
    private final String WORKSPACE = "ne";
    private final String USERNAME = "admin";
    private final String PASSWORD = "geoserver";

    private final RestTemplate restTemplate = new RestTemplate();

//    @Value("${geoserver.url}")
//    private String geoserverUrl;

    private final String username = "admin";
    private final String password = "geoserver";

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public boolean layerExists(String layerName) {
        String url = geoserverUrl + "/rest/layers/" + layerName;
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(buildHeaders()), String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }



    public boolean layerGroupExists(String groupName) {
        String url = geoserverUrl + "/rest/layergroups/" + groupName;
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(buildHeaders()), String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }


    /**
     * 创建图层组（包含多个已发布的图层）
     */
    @Override
    public void createLayerGroup(LayerGroupRequest req) {
        List<LayerItem> items = req.getItems();
        List<String> layers = items.stream().map(LayerItem::getLayer).collect(Collectors.toList());
        List<String> styles = items.stream().map(LayerItem::getStyle).collect(Collectors.toList());

//        Map<String, Object> group = new LinkedHashMap<>();
//        group.put("name", req.getName());
//        group.put("layers", layers);
//        group.put("styles", styles);
//
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("layerGroup", group);

        LayerGroupPayload payload = new LayerGroupPayload();
        LayerGroupPayload.LayerGroup temp = new LayerGroupPayload.LayerGroup();
        temp.setName(req.getName());

        LayerGroupPayload.Layers layers1 = new LayerGroupPayload.Layers();
        layers1.setLayer(layers);
        temp.setLayers(layers1);

        LayerGroupPayload.Styles styles1= new LayerGroupPayload.Styles();
        styles1.setStyle(styles);
        temp.setStyles(styles1);
        payload.setLayerGroup(temp);


        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(USERNAME, PASSWORD);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LayerGroupPayload> entity = new HttpEntity<>(payload, headers);

        String url = geoserverUrl + "/rest/workspaces/" + WORKSPACE + "/layergroups";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("图层组创建失败: " + response.getBody());
        }
    }




    @Override
    public void createLayer(LayerItem item) {
        // 此处省略：你应根据具体 GeoServer 的 publish layer 流程封装发布 WMS/WFS 图层
        // 可以调用 coverageStores、datastores、featuretypes 等 API
    }

    /**
     * 更新图层组（可用于刷新 bbox 或替换图层）
     */
    public void updateLayerGroup(String groupName, List<LayerItem> items, BoundingBox bbox) {
        List<String> layers = items.stream().map(LayerItem::getLayer).collect(Collectors.toList());
        List<String> styles = items.stream().map(LayerItem::getStyle).collect(Collectors.toList());

        Map<String, Object> bounds = new LinkedHashMap<>();
        bounds.put("minx", bbox.getMinx());
        bounds.put("maxx", bbox.getMaxx());
        bounds.put("miny", bbox.getMiny());
        bounds.put("maxy", bbox.getMaxy());
        bounds.put("crs", "EPSG:4326");

        Map<String, Object> group = new LinkedHashMap<>();
        group.put("name", groupName);
        group.put("layers", layers);
        group.put("styles", styles);
        group.put("bounds", bounds);

        Map<String, Object> payload = new HashMap<>();
        payload.put("layerGroup", group);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(USERNAME, PASSWORD);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        String url = geoserverUrl + "/rest/workspaces/" + WORKSPACE + "/layergroups/" + groupName;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("图层组更新失败: " + response.getBody());
        }
    }

    public void updateLayerGroupBoundingBox(String groupName, BoundingBox bbox) {
        // 可以调用 REST PUT 更新图层组 bbox
        // /rest/layergroups/{groupName}
        String url = geoserverUrl + "/layergroups/" + groupName;

        Map<String, Object> bounds = new HashMap<>();
        bounds.put("minx", bbox.getMinx());
        bounds.put("miny", bbox.getMiny());
        bounds.put("maxx", bbox.getMaxx());
        bounds.put("maxy", bbox.getMaxy());
        bounds.put("crs", "EPSG:4326");

        Map<String, Object> layerGroup = new HashMap<>();
        layerGroup.put("name", groupName);
        layerGroup.put("bounds", bounds);

        Map<String, Object> payload = new HashMap<>();
        payload.put("layerGroup", layerGroup);

//        HttpHeaders headers = geoConfig.authHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, buildHeaders());
        restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }


//    /**
//     * 图层组更新时合并已有图层 + 新传入图层
//     */
//    public void mergeIntoLayerGroup(LayerGroupRequest newRequest) throws JsonProcessingException {
//
//        List<LayerItem> items = newRequest.getItems();
//        List<String> layers = items.stream().map(LayerItem::getLayer).collect(Collectors.toList());
//        List<String> styles = items.stream().map(LayerItem::getStyle).collect(Collectors.toList());
//
//        Map<String, Object> layerGroup = new HashMap<>();
//        layerGroup.put("name", newRequest.getName());
//        layerGroup.put("mode", "SINGLE");
//        layerGroup.put("layers", layers);
//        layerGroup.put("styles", styles);
//
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("layerGroup", layerGroup);
//
//        System.out.println(new ObjectMapper().writeValueAsString(payload));
//
////        String xml = buildLayerGroupXml(newRequest);
////        HttpHeaders headers = new HttpHeaders();
////        headers.setBasicAuth("admin", "geoserver");
////        headers.setContentType(MediaType.APPLICATION_XML);
//
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, buildHeaders());
////        HttpEntity<String> entity = new HttpEntity<>(xml, headers);
//
//        String url = geoserverUrl + "/rest/workspaces/" + "ne" + "/layergroups/" + newRequest.getName();
//        restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
//    }



    public void mergeIntoLayerGroup(LayerGroupRequest newRequest) throws Exception {
        String url = geoserverUrl + "/rest/workspaces/ne/layergroups/" + newRequest.getName();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "geoserver");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

        // Step 1: 获取旧图层组 XML
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        String oldXml = response.getBody();

        // Step 2: 解析旧 layer + style
        List<String> existingLayers = parseLayersFromXml(oldXml);
        List<String> existingStyles = parseStylesFromXml(oldXml);

        // Step 3: 合并
//        List<LayerItem> items = newRequest.getItems();
        for (LayerItem item : newRequest.getItems()) {
            if (!existingLayers.contains(item.getLayer())) {
                existingLayers.add(item.getLayer());
                existingStyles.add(item.getStyle());
            }
        }


        // Step 4: 生成新 XML
        String mergedXml = buildLayerGroupXml(newRequest.getName(), existingLayers, existingStyles);

        // Step 5: PUT 更新
        HttpHeaders putHeaders = new HttpHeaders();
        putHeaders.setBasicAuth("admin", "geoserver");
        putHeaders.setContentType(MediaType.APPLICATION_XML);

        HttpEntity<String> entity = new HttpEntity<>(mergedXml, putHeaders);
        restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }




    public Object getLayerGroup(String name) {
        String url = geoserverUrl + "/rest/layergroups/" + name + ".json";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(buildHeaders()), String.class);
        return response.getBody();
    }

    public void deleteLayerGroup(String name) {
        String url = geoserverUrl + "/rest/layergroups/" + name;
        restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(buildHeaders()), Void.class);
    }


    @Data
    public static class BoundingBox {
        private double minx;
        private double miny;
        private double maxx;
        private double maxy;
    }

//    public String buildLayerGroupXml(LayerGroupRequest request) {
//        StringBuilder xml = new StringBuilder();
//        xml.append("<layerGroup>");
//        xml.append("<name>").append(request.getName()).append("</name>");
//        xml.append("<mode>SINGLE</mode>");
//
//        xml.append("<layers>");
//        for (LayerItem item : request.getItems()) {
//            xml.append("<layer>").append(item.getLayer()).append("</layer>");
//        }
//        xml.append("</layers>");
//
//        xml.append("<styles>");
//        for (LayerItem item : request.getItems()) {
//            xml.append("<style>").append(item.getStyle()).append("</style>");
//        }
//        xml.append("</styles>");
//
//        xml.append("</layerGroup>");
//        return xml.toString();
//    }

    public String buildLayerGroupXml(String name, List<String> layers, List<String> styles) {
        StringBuilder sb = new StringBuilder();
        sb.append("<layerGroup><name>").append(name).append("</name>");
        sb.append("<mode>SINGLE</mode><layers>");
        for (String layer : layers) {
            sb.append("<layer>").append(layer).append("</layer>");
        }
        sb.append("</layers><styles>");
        for (String style : styles) {
            sb.append("<style>").append(style).append("</style>");
        }
        sb.append("</styles></layerGroup>");
        return sb.toString();
    }

    public List<String> parseLayersFromXml(String xml) throws Exception {
        List<String> layers = new ArrayList<>();

        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(xml)));

        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile("//publishables/published/name");

        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            String value = nodes.item(i).getTextContent().trim();
            if (!value.isEmpty()) {
                layers.add(value);
            }
        }

        return layers;
    }

    public List<String> parseStylesFromXml(String xml) throws Exception {
        List<String> styles = new ArrayList<>();

        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(xml)));

        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile("//styles/style/name");

        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            String value = nodes.item(i).getTextContent().trim(); // 🔧 加上 trim()
            if (!value.isEmpty()) {
                styles.add(value);
            }
        }

        return styles;
    }


//    public List<String> parseXmlByTag(String xml, String tagName) throws Exception {
//        List<String> result = new ArrayList<>();
//        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
//                .parse(new InputSource(new StringReader(xml)));
//
//        XPath xpath = XPathFactory.newInstance().newXPath();
//        XPathExpression expr = xpath.compile("//" + tagName);
//        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            String value = nodeList.item(i).getTextContent().trim();
//            if (!value.isEmpty()) {
//                result.add(value);
//            }
//        }
//        return result;
//    }
}

