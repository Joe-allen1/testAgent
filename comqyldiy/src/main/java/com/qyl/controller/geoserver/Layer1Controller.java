//package com.qyl.controller.geoserver;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.io.IOException;
//import java.sql.SQLException;
//
//
//@RestController
//@RequestMapping("/layer")
//@RequiredArgsConstructor
//public class Layer1Controller {
//
//    @GetMapping("/wms")
//    public ResponseEntity<byte[]> proxyWmsRequest(@RequestParam MultiValueMap<String, String> queryParams) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // 拼接 GeoServer 请求 URL
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GEOSERVER_WMS_URL)
//                .queryParams(queryParams);
//
//        // 发起请求
//        ResponseEntity<byte[]> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                null,
//                byte[].class
//        );
//        // 返回内容类型一致的响应（通常是 image/png）
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(response.getHeaders().getContentType());
//
//        return new ResponseEntity<>(response.getBody(), headers, response.getStatusCode());
//    }
//
//    private final GeoLayerService svc;
//
//    /* 覆盖整张表 */
//    @PostMapping("/replace")
//    public String replace(
//            @RequestBody String geoJson) throws IOException, SQLException {
//
//        int n = svc.overwriteLayer("your_table_name", geoJson);
//        return "成功导入 " + n + " 条记录";
//    }
//
//    /* 更新默认样式 */
//    @PutMapping("/{workspace}/{layer}/style/{style}")
//    public void changeStyle(@PathVariable String workspace,
//                            @PathVariable String layer,
//                            @PathVariable String style) {
//        svc.updateLayerStyle(workspace, layer, style);
//    }
//
//    /* 修改 FeatureType 元数据 */
//    @PatchMapping("/faf")
//    public void patchMeta(
//
//            @RequestBody MetadataDto dto) {
//        svc.updateFeatureTypeMeta("nc", "postgis1", "qylNewTitle",
//                dto.getTitle(), dto.getSrs());
//    }
//
//    private static final String GEOSERVER_WMS_URL = "http://10.0.10.70:8085/geoserver/wms";
//
//
//}
//
////    EPSG:404000
////    postgis1
//
//
//
////
////    /* 单条记录更新示例（JSON ≈ {"fid":1,"props":{...},"wkt":"POINT(...)"}） */
////    @PatchMapping("/{table}/feature")
////    public void updateOne(@PathVariable String table,
////                          @RequestBody UpdateFeatureDto dto) throws Exception {
////
////        Geometry g = dto.getWkt()==null ? null
////                : new WKTReader().read(dto.getWkt());
////
////        svc.updateFeature(table, "fid", dto.getFid(),
////                dto.getProps(), g);
////    }
//
//
//
////{
////        "type":"FeatureCollection",
////        "features":[
////        {
////        "type":"Feature",
////        "geometry":{
////        "type":"Point",
////        "coordinates":[102.0,0.5]
////        },
////        "properties":{
////        "name":"Example Point"
////        }
////        }
////        ]
////        }