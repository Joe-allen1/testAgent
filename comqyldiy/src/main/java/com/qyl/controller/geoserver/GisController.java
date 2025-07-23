//package com.qyl.controller.geoserver;
//
//import com.ruoyi.common.core.web.controller.BaseController;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.geotools.data.DataStore;
//import org.geotools.data.DataStoreFinder;
//import org.geotools.data.FeatureWriter;
//import org.geotools.data.Transaction;
//import org.geotools.data.simple.SimpleFeatureCollection;
//import org.geotools.data.simple.SimpleFeatureIterator;
//import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
//import org.geotools.geojson.feature.FeatureJSON;
//import org.opengis.feature.simple.SimpleFeature;
//import org.opengis.feature.simple.SimpleFeatureType;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.HashMap;
//import java.util.Map;
//@Slf4j
//@RequestMapping("/safety/layer")
//@Tag(name = "03-【安全能力】02-gis管理")
//@RestController
//public class GisController extends BaseController {
//
//
//    @Operation(summary = "新增几何数据")
//    @PostMapping("/add")
//    public ResponseEntity<String> uploadGeometry(@RequestBody String geoJson) throws IOException {
//        // 1. 将 GeoJSON 转为 SimpleFeatureCollection（使用 GeoTools）
//        FeatureJSON fjson = new FeatureJSON();
//        SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) fjson.readFeatureCollection(new StringReader(geoJson));
//
//        // 2. 保存到数据库（推荐用PostGIS），这里可使用JDBC或Hibernate空间扩展（hibernate-spatial）
//        saveToPostGIS(featureCollection, "your_table_name");
//
//        // 3. 调用GeoServer REST API 发布图层
//        publishToGeoServer("your_table_name");
//
//        return ResponseEntity.ok("图层发布成功");
//    }
//
//
//    public void saveToPostGIS(SimpleFeatureCollection features, String tableName) throws IOException {
//        // 使用 GeoTools 连接 PostGIS 数据库
//        Map<String, Object> params = new HashMap<>();
//        params.put("dbtype", "postgis");
//        params.put("host", "10.0.10.200");
//        params.put("port", 5432);
//        params.put("schema", "public");
//        params.put("database", "qyl");
//        params.put("user", "postgres");
//        params.put("passwd", "spacemv@2023!@");
//
//        DataStore dataStore = DataStoreFinder.getDataStore(params);
//
////        SimpleFeatureType schema = features.getSchema();
////        dataStore.createSchema(schema); // 创建表
//
//
//        SimpleFeatureType original = features.getSchema();
//        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
//        tb.init(original);                  // 复制原有字段结构
//        tb.setName(tableName);              // 把 typeName 改成你想要的表名
//        SimpleFeatureType targetType = tb.buildFeatureType();
//
//        dataStore.createSchema(targetType); // 用正确的名字建表
//
//
//
//
//        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
//                     dataStore.getFeatureWriterAppend(tableName, Transaction.AUTO_COMMIT)) {
//            try (SimpleFeatureIterator it = features.features()) {
//                while (it.hasNext()) {
//                    SimpleFeature source = it.next();
//                    SimpleFeature target = writer.next();
//                    target.setAttributes(source.getAttributes());
//                    writer.write();
//                }
//            }
//        }
//    }
//
//    public void publishToGeoServer(String layerName) {
//        String geoserverUrl = "http://10.0.10.70:8085/geoserver/rest/workspaces/nc/datastores/postgis1/featuretypes";
//        String xmlBody = "<featureType><name>" + layerName + "</name><nativeName>" + layerName + "</nativeName></featureType>";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_XML);
//        headers.setBasicAuth("admin", "geoserver");
//
//        HttpEntity<String> entity = new HttpEntity<>(xmlBody, headers);
//        ResponseEntity<String> response = new RestTemplate().postForEntity(geoserverUrl, entity, String.class);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            System.out.println("图层发布成功！");
//        } else {
//            throw new RuntimeException("GeoServer发布失败：" + response.getBody());
//        }
//    }
//
//}
