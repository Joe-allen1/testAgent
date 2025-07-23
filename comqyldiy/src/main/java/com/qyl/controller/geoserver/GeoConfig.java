//package com.qyl.controller.geoserver;
//
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Configuration
//public class GeoConfig {
//
//    @Bean
//    public DataStore postgisDataStore() throws IOException {
//        Map<String, Object> p = Map.of(
//                "dbtype",  "postgis",
//                "host",    "10.0.10.200",
//                "port",    5432,
//                "schema",  "public",
//                "database","qyl",
//                "user",    "postgres",
//                "passwd",  "spacemv@2023!@"
//        );
//        DataStore ds = DataStoreFinder.getDataStore(p);
//        if (ds == null) throw new IllegalStateException("PostGIS DataStore 创建失败");
//        return ds;
//    }
//
//    @Bean            // 供调用 GeoServer REST
//    public RestTemplate restTemplate(RestTemplateBuilder b){
//        return b.basicAuthentication("admin","geoserver").build();
//    }
//}
//
