//package com.qyl.controller.geoserver;
//
//import com.vividsolutions.jts.geom.Coordinate;
//import com.vividsolutions.jts.geom.GeometryFactory;
//import com.vividsolutions.jts.geom.Point;
//import org.geotools.data.DefaultTransaction;
//import org.geotools.data.wfs.WFSDataStore;
//import org.geotools.data.wfs.WFSDataStoreFactory;
//import org.geotools.feature.simple.SimpleFeatureBuilder;
//import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
//import org.geotools.referencing.crs.DefaultGeographicCRS;
//import org.opengis.feature.simple.SimpleFeature;
//import org.opengis.feature.simple.SimpleFeatureType;
//
//import java.net.URL;
//import java.util.HashMap;
//
//public class TestController {
//
//
////        public static void main(String[] args) {
////            // GeoServer 基本配置
////            String geoserverUrl = "http://10.0.10.70:8085/geoserver/rest";
////            String username = "admin";
////            String password = "geoserver";
////            String styleName = "qyl_style";
////            String sldContent = """
////                <?xml version="1.0" encoding="UTF-8"?>
////                <StyledLayerDescriptor version="1.0.0"
////                    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
////                    xmlns="http://www.opengis.net/sld"
////                    xmlns:ogc="http://www.opengis.net/ogc"
////                    xmlns:xlink="http://www.w3.org/1999/xlink"
////                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
////                  <NamedLayer>
////                    <Name>Simple polygon</Name>
////                    <UserStyle>
////                      <Title>A red fill style</Title>
////                      <FeatureTypeStyle>
////                        <Rule>
////                          <PolygonSymbolizer>
////                            <Fill>
////                              <CssParameter name="fill">#FF0000</CssParameter>
////                            </Fill>
////                          </PolygonSymbolizer>
////                        </Rule>
////                      </FeatureTypeStyle>
////                    </UserStyle>
////                  </NamedLayer>
////                </StyledLayerDescriptor>
////                """;
////            // Step 1: 注册样式元数据（POST /styles）
////            try {
////                String authHeader = "Basic " + Base64.getEncoder()
////                        .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
////
////                RestTemplate restTemplate = new RestTemplate();
////
////                // 样式元信息 JSON（或 XML）
////                String styleBody = """
////                <style>
////                    <name>%s</name>
////                    <filename>%s.sld</filename>
////                </style>
////                """.formatted(styleName, styleName);
////
////                HttpHeaders headers = new HttpHeaders();
////                headers.setContentType(MediaType.APPLICATION_XML);
////                headers.set("Authorization", authHeader);
////
////                HttpEntity<String> styleEntity = new HttpEntity<>(styleBody, headers);
////                String styleUrl = geoserverUrl + "/styles";
////                ResponseEntity<String> response = restTemplate.postForEntity(styleUrl, styleEntity, String.class);
////                System.out.println("样式注册状态: " + response.getStatusCode());
////
////                // Step 2: 上传 SLD 内容（PUT /styles/{styleName})
////                HttpHeaders putHeaders = new HttpHeaders();
////                putHeaders.setContentType(MediaType.valueOf("application/vnd.ogc.sld+xml"));
////                putHeaders.set("Authorization", authHeader);
////
////                HttpEntity<String> sldEntity = new HttpEntity<>(sldContent, putHeaders);
////                String uploadUrl = geoserverUrl + "/styles/" + styleName;
////                restTemplate.put(uploadUrl, sldEntity);
////                System.out.println("SLD 文件上传成功！");
////
////            } catch (Exception e) {
////                System.err.println("上传失败: " + e.getMessage());
////            }
////        }
////    public static void main(String[] args) {
////        // 1. GeoServer REST API URL 和认证信息
////        String geoserverUrl = "http://10.0.10.70:8085/geoserver/rest";  // 替换为你实际的 GeoServer 地址
////        String username = "admin";
////        String password = "geoserver";
////
////        // 2. 创建图层所需的请求体数据 (JSON 格式)
////        String jsonInputString = "{\n" +
////                "  \"featureType\": {\n" +
////                "    \"name\": \"newLayer\",\n" +
////                "    \"nativeCRS\": \"EPSG:4326\",\n" +
////                "    \"srs\": \"EPSG:4326\",\n" +
////                "    \"geometryType\": \"Point\",\n" +
////                "    \"attributes\": [\n" +
////                "      {\"name\": \"name\", \"binding\": \"java.lang.String\"},\n" +
////                "      {\"name\": \"location\", \"binding\": \"org.opengis.geometry.Position\"}\n" +
////                "    ]\n" +
////                "  }\n" +
////                "}";
////
////        // 3. GeoServer REST API 图层创建 URL
////        String urlString = geoserverUrl + "/test/datastores/test/featuretypes.json";  // 需替换为实际工作空间和图层存储
////
////        try {
////            // 4. 设置 URL 和连接
////            URL url = new URL(urlString);
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////
////            // 5. 设置请求方法为 POST
////            connection.setRequestMethod("POST");
////            connection.setRequestProperty("Content-Type", "application/json");
////            String auth = username + ":" + password;
////            String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
////            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);  // 使用基本身份验证
////            connection.setDoOutput(true);
////
////            // 6. 发送请求体数据
////            try (OutputStream os = connection.getOutputStream()) {
////                byte[] input = jsonInputString.getBytes("utf-8");
////                os.write(input, 0, input.length);
////            }
////
////            // 7. 获取响应码
////            int responseCode = connection.getResponseCode();
////            System.out.println("HTTP Response Code: " + responseCode);
////
////            // 8. 读取响应数据
////            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
////                StringBuilder response = new StringBuilder();
////                String responseLine;
////                while ((responseLine = br.readLine()) != null) {
////                    response.append(responseLine.trim());
////                }
////                System.out.println("Response: " + response.toString());
////            }
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//
//
//    public static void main(String[] args) {
//        try {
//            // 1. 设置WFS服务的URL
//            URL wfsURL = new URL("http://10.0.10.70:8085/geoserver/ows?service=WFS&version=2.0.0&request=GetCapabilities");
//
//            // 2. 创建WFS数据存储
//            WFSDataStore dataStore = (WFSDataStore) new WFSDataStoreFactory()
//                    .createDataStore(new HashMap<String, Object>() {{
//                        put(WFSDataStoreFactory.URL.key, wfsURL);
//                        put(WFSDataStoreFactory.USERNAME.key, "admin");
//                        put(WFSDataStoreFactory.PASSWORD.key, "geoserver");
//                    }});
//
//            // 3. 获取WFS服务中可用的图层名称
//            String[] featureTypes = dataStore.getTypeNames();
//            System.out.println("Available feature types: " + featureTypes);
//
//            // 4. 创建要新增的图层的FeatureType
//            SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
//            builder.setName("nc:nc_communication_point111");
//            builder.setCRS(DefaultGeographicCRS.WGS84);  // 设置坐标系
//            builder.add("geometry", Point.class);  // 假设是一个包含坐标点的图层
//            builder.add("name", String.class);  // 假设包含名字字段
//
//            SimpleFeatureType featureType = builder.buildFeatureType();
//
//            // 5. 创建要新增的 Feature
//            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
//            featureBuilder.add(new GeometryFactory().createPoint(new Coordinate(0, 0)));  // 设置坐标
//            featureBuilder.add("New Point");  // 设置名称字段
//
//            SimpleFeature feature = featureBuilder.buildFeature(null);
//
//            // 6. 创建一个WFS事务
//            DefaultTransaction defaultTransaction = new DefaultTransaction();
//
//            dataStore.getFeatureWriter("nc:nc_communication_point111", defaultTransaction).write();
//
//
//            defaultTransaction.commit();
//            System.out.println("Feature successfully added to WFS service!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}
//
