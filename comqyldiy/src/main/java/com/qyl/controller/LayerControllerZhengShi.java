package com.qyl.controller;//package org.tianxun.controller;

import com.qyl.service.LayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/layer")
@Tag(name = "01-【图层】00-图层")
public class LayerControllerZhengShi {

    @Autowired
    private LayService layService;

    @GetMapping("/select")
    public ResponseEntity<byte[]> select(@RequestParam MultiValueMap<String, String> queryParams) {
        ResponseEntity<byte[]> reponse = layService.select(queryParams);
        return reponse;
    }
}
