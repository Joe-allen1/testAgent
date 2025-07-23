package com.qyl.service;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public interface LayService {
    ResponseEntity<byte[]> select(MultiValueMap<String, String> queryParams);
}
