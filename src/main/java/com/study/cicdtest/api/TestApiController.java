package com.study.cicdtest.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApiController {

    @GetMapping("/")
    public ResponseEntity<?> test(){

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
