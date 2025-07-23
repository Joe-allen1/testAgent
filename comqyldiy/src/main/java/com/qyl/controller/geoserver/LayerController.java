//package com.qyl.controller.geoserver;
//
//import com.ruoyi.common.core.web.controller.BaseController;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.net.ssl.HttpsURLConnection;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Base64;
//
//@Slf4j
//@RequestMapping("/safety/layer")
//@Tag(name = "03-【安全能力】02-图层管理")
//@RestController
//public class LayerController extends BaseController {
//
//
//    private static final String GEOSERVER_REST_URL = "http://10.0.10.70:8085/geoserver/rest/";
//    private static final String USERNAME = "admin";
//    private static final String PASSWORD = "geoserver";
//
//    public static void main(String[] args) {
//        try {
//            // 数据存储名称和图层名称
//            String workspace = "test";
//            String datastoreName = "qyl0625";
//            String layerName = "qyl414";
//            String wfsUrl = "http://10.0.10.70:8085/geoserver/wfs?service=WFS&request=GetCapabilities"; // 远程 WFS 服务 URL
//
//            // 1. 创建数据存储
//            createWFSDataStore(workspace, datastoreName, wfsUrl);
//
//            // 2. 创建图层
//            createLayer(workspace, datastoreName, layerName);
//
//            // 3. 发布图层
//            publishLayer(workspace, layerName);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 创建数据存储
//    private static void createWFSDataStore(String workspace, String datastoreName, String wfsUrl) throws Exception {
//        String url = GEOSERVER_REST_URL + "workspaces/" + workspace + "/datastores";
//
//        String body = "<dataStore>" +
//                "<name>" + datastoreName + "</name>" +
//                "<connectionParameters>" +
//                "<entry key=\"wfs_url\">" + wfsUrl + "</entry>" +
//                "<entry key=\"typename\">layer_from_wfs</entry>" +
//                "</connectionParameters>" +
//                "</dataStore>";
//
//        sendPostRequest(url, body);
//    }
//
//    // 创建图层
//    private static void createLayer(String workspace, String datastoreName, String layerName) throws Exception {
//        String url = GEOSERVER_REST_URL + "workspaces/" + workspace + "/datastores/" + datastoreName + "/featuretypes";
//
//        String body = "<featureType>" +
//                "<name>" + layerName + "</name>" +
//                "<title>" + layerName + " Title</title>" +
//                "</featureType>";
//
//        sendPostRequest(url, body);
//    }
//
//    // 发布图层
//    private static void publishLayer(String workspace, String layerName) throws Exception {
//        String url = GEOSERVER_REST_URL + "workspaces/" + workspace + "/layers/" + layerName + "/publish";
//
//        String body = "<layer>" +
//                "<enabled>true</enabled>" +
//                "</layer>";
//
//        sendPutRequest(url, body);
//    }
//
//    // 发送 POST 请求
//    private static void sendPostRequest(String url, String body) throws Exception {
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//        con.setRequestMethod("POST");
//        con.setDoOutput(true);
//        con.setRequestProperty("Content-Type", "application/xml");
//        con.setRequestProperty("Authorization", "Basic " + encodeCredentials(USERNAME, PASSWORD));
//
//        try (OutputStream os = con.getOutputStream()) {
//            byte[] input = body.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//
//        int responseCode = con.getResponseCode();
//        System.out.println("POST Response Code: " + responseCode);
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            System.out.println(response.toString());
//        }
//    }
//
//    // 发送 PUT 请求
//    private static void sendPutRequest(String url, String body) throws Exception {
//        URL obj = new URL(url);
//        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
//
//        con.setRequestMethod("PUT");
//        con.setDoOutput(true);
//        con.setRequestProperty("Content-Type", "application/xml");
//        con.setRequestProperty("Authorization", "Basic " + encodeCredentials(USERNAME, PASSWORD));
//
//        try (OutputStream os = con.getOutputStream()) {
//            byte[] input = body.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//
//        int responseCode = con.getResponseCode();
//        System.out.println("PUT Response Code: " + responseCode);
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            System.out.println(response.toString());
//        }
//    }
//
//    // Base64 编码用户名和密码
//    private static String encodeCredentials(String username, String password) {
//        String auth = username + ":" + password;
//        return new String(Base64.getEncoder().encode(auth.getBytes()));
//    }
//
//
//}
