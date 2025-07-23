//package com.qyl.controller.geoserver;
//
//import com.ruoyi.common.core.utils.StringUtils;
//import com.ruoyi.common.core.web.controller.BaseController;
//import com.ruoyi.common.core.web.domain.AjaxResult;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.geotools.factory.CommonFactoryFinder;
//import org.geotools.styling.*;
//import org.geotools.xml.styling.SLDTransformer;
//import org.opengis.filter.FilterFactory2;
//import org.springframework.beans.BeanUtils;
//import org.springframework.web.bind.annotation.*;
//import org.tianxun.domian.dto.InsertGeoServerStyleDto;
//import org.tianxun.domian.dto.UpdateGeoServerStyleDto;
//
//import javax.validation.Valid;
//import java.awt.*;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.Base64;
//
//@Slf4j
//@RequestMapping("/safety/style")
//@Tag(name = "03-【安全能力】01-样式管理")
//@RestController
//public class StyleController extends BaseController {
//
//    private static final String GEOSERVER_REST_URL = "http://10.0.10.70:8085/geoserver/rest";
//    private static final String USERNAME = "admin";
//    private static final String PASSWORD = "geoserver";
//
//    private final String AUTH = Base64.getEncoder()
//            .encodeToString("admin:geoserver".getBytes(StandardCharsets.UTF_8));
//
//    @Operation(summary = "新增样式")
//    @PostMapping("/add")
//    public AjaxResult add(@Valid @RequestBody InsertGeoServerStyleDto p) throws Exception {
//        // 1) 构造 GeoTools Style → SLD XML
//        Style styleObj = buildStyleFromParams(p);
//        String sldXml  = styleToSld(styleObj);
//
//        // 2) POST 到 upsert 端点
//        String url = String.format(
//                "%s/workspaces/%s/styles?name=%s",
//                GEOSERVER_REST_URL, "test", p.getStyleName()
//        );
//        HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
//        conn.setRequestMethod("POST");  // 一定要 POST
//        conn.setRequestProperty("Content-Type", "application/vnd.ogc.sld+xml");
//        conn.setRequestProperty("Authorization", AUTH_HEADER);
//        conn.setDoOutput(true);
//
//        try (OutputStream os = conn.getOutputStream()) {
//            os.write(sldXml.getBytes(StandardCharsets.UTF_8));
//        }
//
//        int code = conn.getResponseCode();
//        if (code < 200 || code >= 300) {
//            String error = new String(conn.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
//            throw new RuntimeException("新增样式失败 HTTP " + code + " — " + error);
//        }
//        return success("新增成功");
//    }
//
//    @Operation(summary = "查看样式")
//    @PostMapping("/select")
//    public AjaxResult select(String styleName) {
//        String sld = null;
//        try {
//            // 你要查询的样式名
////            String styleName = "qyl_style";
//            // 1) 获取 SLD XML
//            sld = getStyleSLD(styleName);
//            System.out.println("=== SLD for style \"" + styleName + "\" ===");
//            System.out.println(sld);
//
//            // 2) 如果你想要 JSON 格式元数据，也可以调用 below 方法
//            String metaJson = getStyleJson(styleName);
//            System.out.println("=== Metadata JSON for style \"" + styleName + "\" ===");
//            System.out.println(metaJson);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return success(sld);
//    }
//
//    @Operation(summary = "修改样式")
//    @PutMapping("/update")
//    public AjaxResult update(@Valid @RequestBody UpdateGeoServerStyleDto params) throws Exception {
//        InsertGeoServerStyleDto insertGeoServerStyleDto = new InsertGeoServerStyleDto();
//        BeanUtils.copyProperties(params, insertGeoServerStyleDto);
//        modifyStyle(params.getOldWorkSpaceName(), params.getOldName(), params.getNewWorkSpaceName(), params.getNewName(), insertGeoServerStyleDto);
//        return success("修改成功");
//    }
//
//
//    @Operation(summary = "删除样式")
//    @DeleteMapping("/delete")
//    public AjaxResult delete(String workspace, String styleName) throws Exception {
//        // 1) 构造正确的 URL
//        String base;
//        if (workspace != null && !workspace.isBlank()) {
//            base = String.format("%s/workspaces/%s/styles/%s.json", GEOSERVER_REST_URL, workspace, styleName);
//        } else {
//            base = String.format("%s/styles/%s.json", GEOSERVER_REST_URL, styleName);
//        }
//        String url = base + "?recurse=" + true;
//        // 2) 打开连接并设置 DELETE
//        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//        conn.setRequestMethod("DELETE");
//        conn.setRequestProperty("Authorization", AUTH_HEADER);
//        // 3) 执行并检查响应码
//        int code = conn.getResponseCode();
//        if (code >= 200 && code < 300) {
//            log.info("样式删除成功: HTTP " + code);
//            System.out.println("样式删除成功: HTTP " + code);
//        } else {
//            // 读取错误详情
//            String error = "";
//            InputStream errStream = conn.getErrorStream();
//            if (errStream != null) {
//                error = new String(errStream.readAllBytes(), StandardCharsets.UTF_8);
//            }
//            log.info("样式删除失败: HTTP " + code + " — " + error);
//            throw new RuntimeException("删除样式失败: HTTP " + code + " — " + error);
//        }
//        // 4) 断开连接
//        conn.disconnect();
//        return success("删除成功");
//    }
//    /**
//     * GET /rest/styles/{styleName}.sld
//     */
//    public String getStyleSLD(String styleName) throws Exception {
//        String urlStr = GEOSERVER_REST_URL + "/styles/" + styleName + ".sld";
//        HttpURLConnection conn = openGetConnection(urlStr, "application/vnd.ogc.sld+xml");
//        return readResponse(conn);
//    }
//
//    /**
//     * GET /rest/styles/{styleName}.json
//     */
//    public String getStyleJson(String styleName) throws Exception {
//        String urlStr = GEOSERVER_REST_URL + "/styles/" + styleName + ".json";
//        HttpURLConnection conn = openGetConnection(urlStr, "application/json");
//        return readResponse(conn);
//    }
//
//    // 公共：打开一个 GET 连接并设置认证和 Accept header
//    private HttpURLConnection openGetConnection(String urlStr, String accept) throws Exception {
//        URL url = new URL(urlStr);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", accept);
//        // Basic Auth
//        String auth = USERNAME + ":" + PASSWORD;
//        String encoded = Base64.getEncoder()
//                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));
//        conn.setRequestProperty("Authorization", "Basic " + encoded);
//        return conn;
//    }
//
//    // 读取响应体成 String
//    private String readResponse(HttpURLConnection conn) throws Exception {
//        int status = conn.getResponseCode();
//        if (status >= 200 && status < 300) {
//            try (BufferedReader in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = in.readLine()) != null) {
//                    sb.append(line).append('\n');
//                }
//                return sb.toString();
//            }
//        } else {
//            throw new RuntimeException("请求失败，HTTP status=" + status);
//        }
//    }
//
//
//    /**
//     * 修改样式：
//     * 1) 如果 newName 或 newWorkspace 不为 null，则先重命名/切换 workspace
//     * 2) 如果 styleParams 不为 null，则更新 SLD
//     *
//     * @param oldWorkspace 当前样式所在 workspace
//     * @param oldName      当前样式名称
//     * @param newWorkspace 要切换到的 workspace（不切换传 null）
//     * @param newName      要改成的新名称（不改名传 null）
//     * @param styleParams  用来更新 SLD 的参数（不更新传 null）
//     */
//    public void modifyStyle(String oldWorkspace,
//                                   String oldName,
//                                   String newWorkspace,
//                                   String newName,
//                                   InsertGeoServerStyleDto styleParams) throws Exception {
//
//        // 1) 重命名或切换 workspace
//        if (StringUtils.isNotBlank(newName) || StringUtils.isNotBlank(newWorkspace)) {
//            String url = String.format("%s/workspaces/%s/styles/%s.json",
//                    GEOSERVER_REST_URL, oldWorkspace, oldName);
//
//            // 构造 JSON body
//            StringBuilder body = new StringBuilder("{\"style\":{");
//            boolean first = true;
//            if (StringUtils.isNotBlank(newName)) {
//                body.append("\"name\":\"").append(newName).append("\"");
//                first = false;
//            }
//            if (StringUtils.isNotBlank(newWorkspace)) {
//                if (!first) body.append(",");
//                body.append("\"workspace\":\"").append(newWorkspace).append("\"");
//            }
//            body.append("}}");
//
//            HttpURLConnection conn = open(url, "PUT", "application/json");
//            try (OutputStream os = conn.getOutputStream()) {
//                os.write(body.toString().getBytes(StandardCharsets.UTF_8));
//            }
//            int code = conn.getResponseCode();
//            if (code < 200 || code >= 300) {
//                throw new RuntimeException("重命名样式失败 HTTP " + code);
//            }
//
//            // 更新引用后的 workspace/name
//            if (newWorkspace != null) oldWorkspace = newWorkspace;
//            if (newName      != null) oldName      = newName;
//        }
//
//        // 2) 更新 SLD
//        if (styleParams != null) {
//            // 构建 & 序列化 SLD
//            Style styleObj = buildStyleFromParams(styleParams);
//            String sldXml   = styleToSld(styleObj);
//
//            String sldUrl = String.format("%s/workspaces/%s/styles/%s.sld",
//                    GEOSERVER_REST_URL, oldWorkspace, oldName);
//            HttpURLConnection conn = open(sldUrl, "PUT", "application/vnd.ogc.sld+xml");
//            try (OutputStream os = conn.getOutputStream()) {
//                os.write(sldXml.getBytes(StandardCharsets.UTF_8));
//            }
//            int code = conn.getResponseCode();
//            if (code < 200 || code >= 300) {
//                throw new RuntimeException("更新 SLD 失败 HTTP " + code);
//            }
//        }
//    }
//
//
//    // 打开连接并设置认证、方法、Content-Type
//    private HttpURLConnection open(String url, String method, String contentType) throws Exception {
//        HttpURLConnection c = (HttpURLConnection)new URL(url).openConnection();
//        c.setRequestMethod(method);
//        c.setRequestProperty("Authorization", AUTH_HEADER);
//        if (contentType != null) {
//            c.setRequestProperty("Content-Type", contentType);
//            c.setDoOutput(true);
//        }
//        return c;
//    }
//
//    public static void main(String[] args) throws Exception {
//        InsertGeoServerStyleDto p = new InsertGeoServerStyleDto();
//        p.setStyleName("qyl_style11111111");
//        p.setType(0);
//        p.setFillColor("#00FF00");
//        p.setStrokeColor("#0000FF");
//        p.setStrokeWidth(100);
//        p.setOpacity(0.5);
//        p.setSize(10);
//        p.setName("pts");
//
////        generateAndPush(p);
////        modifyStyle("nc", "txwwqqrq", "test", "txwwqqrq", p);
////        add( p);
//    }
//
//    private final String AUTH_HEADER = "Basic " +
//            Base64.getEncoder()
//                    .encodeToString(("admin:geoserver").getBytes(StandardCharsets.UTF_8));
//
//    public void generateAndPush(InsertGeoServerStyleDto p) throws Exception {
//        // 1) 用 GeoTools 根据参数生成 Style 对象
//        Style style = buildStyleFromParams(p);
//        // 2) 序列化成 SLD XML
//        String sldXml = styleToSld(style);
//        // 3) 推送给 GeoServer
////        String url = GEOSERVER_REST_URL + "/workspaces" + "/test"  + "/styles/" + p.getStyleName() + ".sld";
////        String url = GEOSERVER_REST_URL
////                + "/workspaces/" + "test"
////                + "/styles?name=" + p.getStyleName()
////                + "&overwrite=true";
//
//
//        // 2) upsert 接口 URL (POST + overwrite)
////        String url = GEOSERVER_REST_URL
////                + "/workspaces/test/styles"   // 注意：这里是 /styles, 没有 .sld
////                + "?name=" + p.getStyleName()
////                + "&overwrite=true";
//
//        String url = GEOSERVER_REST_URL
//                + "/workspaces/" + "test"
//                + "/styles/"     + p.getStyleName() + ".sld";
//
//        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//        conn.setRequestMethod("PUT");
//        conn.setRequestProperty("Content-Type", "application/vnd.ogc.sld+xml");
//        conn.setRequestProperty("Authorization", AUTH_HEADER);
//        conn.setDoOutput(true);
//        try (OutputStream os = conn.getOutputStream()) {
//            byte[] bytes = sldXml.getBytes(StandardCharsets.UTF_8);
//            os.write(bytes);
//        }
//        if (conn.getResponseCode() / 100 != 2) {
//            throw new RuntimeException("GeoServer 返回码：" + conn.getResponseCode());
//        }
//    }
//
//    public Style buildStyleFromParams(InsertGeoServerStyleDto p) {
//        StyleFactory SF = CommonFactoryFinder.getStyleFactory();
//        FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();
//
//        // 1. 通用 Fill & Stroke
//        Fill fill = SF.createFill(
//                FF.literal(Color.decode(p.getFillColor())),
//                FF.literal(p.getOpacity())
//        );
//        Stroke stroke = SF.createStroke(
//                FF.literal(Color.decode(p.getStrokeColor())),
//                FF.literal(p.getStrokeWidth()),
//                FF.literal(p.getOpacity())
//        );
//
//        Symbolizer sym;
//
//        switch (p.getType()){
//            case 0:
//                // ——— a) createMark 现在需要 5 参数 ———
//                Mark mark = SF.createMark(
//                        FF.literal("circle"),  // wellKnownName
//                        stroke,                // 边框
//                        fill,                  // 填充
//                        FF.literal(p.getSize()), // 标记大小
//                        FF.literal(0)            // 标记旋转
//                );
//                // ——— b) createGraphic ———
//                Graphic graphic = SF.graphic(
//                        /* marks */           Arrays.asList(new Mark[]{mark}),
//                        /* opacity */         FF.literal(1.0),
//                        /* size */            FF.literal(p.getSize()),
//                        /* rotation */        FF.literal(0),
//                        /* anchorPoint */     null,
//                        /* displacement */    null
//                );
//                // ——— c) PointSymbolizer ———
//                sym = SF.createPointSymbolizer(graphic, null);
//                break;
//
//            case 1:
//                sym = SF.createLineSymbolizer(stroke, null);
//                break;
//
//            case 2:
//                sym = SF.createPolygonSymbolizer(stroke, fill, null);
//                break;
//
//            default:
//                throw new IllegalArgumentException("不支持的几何类型: " + p.getType());
//        }
//
//        // 2. Rule → FeatureTypeStyle → Style
//        Rule rule = SF.createRule();
//        rule.symbolizers().add(sym);
//
//        FeatureTypeStyle fts = SF.createFeatureTypeStyle(new Rule[]{ rule });
//        Style style = SF.createStyle();
//        style.setName(p.getStyleName());
//        style.featureTypeStyles().add(fts);
//        return style;
//    }
//
//    private String styleToSld(Style style) throws Exception {
//        SLDTransformer tx = new SLDTransformer();
//        tx.setIndentation(2);
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//            tx.transform(style, baos);
//            return baos.toString(StandardCharsets.UTF_8.name());
//        }
//    }
//}
//
//
////    // GeoServer 基本配置
////    String geoserverUrl = "http://10.0.10.70:8085/geoserver/rest/workspaces/test";
////    String username = "admin";
////    String password = "geoserver";
////    String styleName = "qyl_style11";
////    String sldContent = """
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
////// Step 1: 注册样式元数据（POST /styles）
////        try {
////                String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
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
////                // 头
////                HttpHeaders headers = new HttpHeaders();
////                headers.setContentType(MediaType.APPLICATION_XML);
////                headers.set("Authorization", authHeader);
////
////                HttpEntity<String> styleEntity = new HttpEntity<>(styleBody, headers);
////        String styleUrl = geoserverUrl + "/styles";
////        ResponseEntity<String> response = restTemplate.postForEntity(styleUrl, styleEntity, String.class);
////        System.out.println("样式注册状态: " + response.getStatusCode());
////
////        // Step 2: 上传 SLD 内容（PUT /styles/{styleName})
////        // 头
////        HttpHeaders putHeaders = new HttpHeaders();
////        putHeaders.setContentType(MediaType.valueOf("application/vnd.ogc.sld+xml"));
////        putHeaders.set("Authorization", authHeader);
////
////        HttpEntity<String> sldEntity = new HttpEntity<>(sldContent, putHeaders);
////        String uploadUrl = geoserverUrl + "/styles/" + styleName;
////        restTemplate.put(uploadUrl, sldEntity);
////
////        System.out.println("SLD 文件上传成功！");
////        } catch (Exception e) {
////        System.err.println("上传失败: " + e.getMessage());
////        }
////        return success("新增成功");