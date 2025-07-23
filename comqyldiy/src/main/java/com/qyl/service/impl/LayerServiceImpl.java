package com.qyl.service.impl;//package org.tianxun.service.impl;

import com.qyl.service.LayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.nio.charset.StandardCharsets;

@Service
public class LayerServiceImpl implements LayService {

    @Value("${geoserver.wms.url}")
    private String GEOSERVER_WMS_URL;

    @Override
    public ResponseEntity<byte[]> select(MultiValueMap<String, String> queryParams) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GEOSERVER_WMS_URL)
                .queryParams(queryParams);

        // 发起请求
        ResponseEntity<byte[]> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                byte[].class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(response.getHeaders().getContentType());
            return new ResponseEntity<>(response.getBody(), headers, response.getStatusCode());
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
                    .body("图层不存在或参数错误!".getBytes(StandardCharsets.UTF_8));
        }
    }
}
