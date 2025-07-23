//package com.qyl.controller.geoserver;
//
//import com.vividsolutions.jts.geom.Geometry;
//import lombok.RequiredArgsConstructor;
//import org.geotools.data.DataStore;
//import org.geotools.data.FeatureWriter;
//import org.geotools.data.Transaction;
//import org.geotools.data.simple.SimpleFeatureCollection;
//import org.geotools.data.simple.SimpleFeatureIterator;
//import org.geotools.factory.CommonFactoryFinder;
//import org.geotools.geojson.feature.FeatureJSON;
//import org.geotools.jdbc.JDBCDataStore;
//import org.opengis.feature.simple.SimpleFeature;
//import org.opengis.feature.simple.SimpleFeatureType;
//import org.opengis.filter.Filter;
//import org.opengis.filter.FilterFactory2;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class GeoLayerService {
//
//    private final DataStore dataStore;      // 注入上面的 bean
//    private final RestTemplate rest;        // ditto
//
//    /* ---------- 3.1 覆盖整张表 ---------- */
//    public int overwriteLayer(String table, String geoJson) throws IOException, SQLException {
//        // 1. 解析 GeoJSON
//        SimpleFeatureCollection fc = (SimpleFeatureCollection)
//                new FeatureJSON().readFeatureCollection(new StringReader(geoJson));
//
//        // 2. 清表
//        try (Connection c = ((JDBCDataStore) dataStore).getDataSource().getConnection();
//             Statement s = c.createStatement()) {
//            s.execute("TRUNCATE TABLE " + table);
//        }
//
//        // 3. 重新写入
//        int count = 0;
//        try (FeatureWriter<SimpleFeatureType,SimpleFeature> w =
//                     dataStore.getFeatureWriterAppend(table, Transaction.AUTO_COMMIT);
//             SimpleFeatureIterator it = fc.features()) {
//            while (it.hasNext()) {
//                SimpleFeature src = it.next();
//                SimpleFeature dst = w.next();
//                dst.setAttributes(src.getAttributes());
//                w.write();
//                count++;
//            }
//        }
//        return count;
//    }
//
//    /* ---------- 3.2 更新单条 / 多条要素 ---------- */
//    public void updateFeature(String table, String fidField, Object fidValue,
//                              Map<String,Object> attrs, Geometry newGeom) throws IOException {
//
//        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
//        Filter filter = ff.equals(ff.property(fidField), ff.literal(fidValue));
//
//        try (FeatureWriter<SimpleFeatureType,SimpleFeature> w =
//                     dataStore.getFeatureWriter(table, filter, Transaction.AUTO_COMMIT)) {
//            while (w.hasNext()) {
//                SimpleFeature f = w.next();
//                attrs.forEach(f::setAttribute);
//                if (newGeom != null) f.setDefaultGeometry(newGeom);
//                w.write();
//            }
//        }
//    }
//
//    /* ---------- 3.3 替换 GeoServer 默认样式 ---------- */
//    public void updateLayerStyle(String ws, String layer, String style) {
//        String url  = String.format(
//                "http://10.0.10.70:8085/geoserver/rest/workspaces/%s/layers/%s", ws, layer);
//
//        String xml  = "<layer><defaultStyle><name>"
//                + style + "</name></defaultStyle></layer>";
//
//        HttpHeaders h = new HttpHeaders();
//        h.setContentType(MediaType.APPLICATION_XML);
//        rest.put(url, new HttpEntity<>(xml, h));     // 200 / 201 表示成功
//    }
//
//    /* ---------- 3.4 修改 FeatureType 元数据 ---------- */
//    public void updateFeatureTypeMeta(String ws, String store, String featureType,
//                                      String newTitle, String newSrs) {
//
//        String url = String.format(
//                "http://10.0.10.70:8085/geoserver/rest/workspaces/%s/datastores/%s/featuretypes/%s",
//                ws, store, featureType);
//
//        String xml = "<featureType>"
//                +   (newTitle != null ? "<title>"+newTitle+"</title>" : "")
//                +   (newSrs   != null ? "<srs>"+newSrs+"</srs>"       : "")
//                + "</featureType>";
//
//        HttpHeaders h = new HttpHeaders();
//        h.setContentType(MediaType.APPLICATION_XML);
//        rest.put(url, new HttpEntity<>(xml, h));
//    }
//}
//
