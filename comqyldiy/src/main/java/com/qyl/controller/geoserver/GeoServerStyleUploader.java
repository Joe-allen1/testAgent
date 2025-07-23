package com.qyl.controller.geoserver;//package org.tianxun.controller.geoserver;
//
//import org.geotools.styling.*;
//import org.geotools.factory.CommonFactoryFinder;
//import org.geotools.xml.styling.SLDTransformer;
//import org.opengis.filter.FilterFactory2;
//import java.awt.Color;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//public class GeoServerStyleUploader {
//
//    // GeoServer 配置
//    static final String GEOSERVER_URL = "http://10.0.10.70:8085/geoserver";
//    static final String REST_URL = GEOSERVER_URL + "/rest";
//    static final String USERNAME = "admin";
//    static final String PASSWORD = "geoserver";
//
//    static final String STYLE_NAME = "point-style";
//    static final String WORKSPACE = "yourWorkspace"; // 替换为你的实际工作空间
//    static final String LAYER_NAME = "yourLayer";     // 替换为你的图层名
//
//    public static void main(String[] args) throws Exception {
//        // 1. 创建样式
//        Style style = buildPointStyle();
//
//        // 2. 写入临时文件
//        File sldFile = new File("point-style.sld");
//        saveStyleToSLD(style, sldFile);
//
//        // 3. 上传 SLD 到 GeoServer
//        uploadSLDToGeoServer(sldFile);
//
//        // 4. 应用样式到图层
//        applyStyleToLayer();
//    }
//
//    // Step 1: 创建红色圆点样式
//    public static Style buildPointStyle() {
//        StyleFactory sf = CommonFactoryFinder.getStyleFactory();
//        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
//
//        Mark mark = sf.mark("circle",
//                sf.fill(ff.literal(Color.RED)),
//                sf.stroke(ff.literal(Color.BLACK), ff.literal(1)));
//
//        Graphic graphic = sf.graphic(new Mark[]{mark}, ff.literal(1.0), ff.literal(10), ff.literal(0), null, null);
//        PointSymbolizer sym = sf.pointSymbolizer("point", null, null, null, graphic);
//
//        Rule rule = sf.rule();
//        rule.symbolizers().add(sym);
//
//        FeatureTypeStyle fts = sf.featureTypeStyle(new Rule[]{rule});
//        Style style = sf.style();
//        style.featureTypeStyles().add(fts);
//        style.setName(STYLE_NAME);
//        return style;
//    }
//
//    // Step 2: 保存 SLD 文件
//    public static void saveStyleToSLD(Style style, File file) throws Exception {
//        SLDTransformer transformer = new SLDTransformer();
//        transformer.setIndentation(2);
//        try (OutputStream out = new FileOutputStream(file)) {
//            transformer.transform(style, out);
//        }
//    }
//
//    // Step 3: 上传样式到 GeoServer
//    public static void uploadSLDToGeoServer(File sldFile) throws Exception {
//        String urlStr = REST_URL + "/styles";
//        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
//        conn.setRequestMethod("POST");
//        conn.setDoOutput(true);
//        conn.setRequestProperty("Content-Type", "application/vnd.ogc.sld+xml");
//        conn.setRequestProperty("Authorization", "Basic " + encodeAuth());
//
//        try (OutputStream os = conn.getOutputStream(); FileInputStream fis = new FileInputStream(sldFile)) {
//            byte[] buffer = new byte[4096];
//            int len;
//            while ((len = fis.read(buffer)) != -1) {
//                os.write(buffer, 0, len);
//            }
//        }
//
//        int code = conn.getResponseCode();
//        System.out.println("Style upload response: " + code);
//        if (code >= 400) throw new IOException("Failed to upload style");
//    }
//
//    // Step 4: 将样式应用到图层
//    public static void applyStyleToLayer() throws Exception {
//        String urlStr = REST_URL + "/layers/" + WORKSPACE + ":" + LAYER_NAME;
//        String body = "{\"layer\": {\"defaultStyle\": {\"name\": \"" + STYLE_NAME + "\"}}}";
//
//        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
//        conn.setRequestMethod("PUT");
//        conn.setDoOutput(true);
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Authorization", "Basic " + encodeAuth());
//
//        try (OutputStream os = conn.getOutputStream()) {
//            os.write(body.getBytes(StandardCharsets.UTF_8));
//        }
//
//        int code = conn.getResponseCode();
//        System.out.println("Apply style response: " + code);
//        if (code >= 400) throw new IOException("Failed to apply style");
//    }
//
//    private static String encodeAuth() {
//        return Base64.getEncoder().encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8));
//    }
//}
//
