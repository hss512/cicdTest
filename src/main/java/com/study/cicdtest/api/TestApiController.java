package com.study.cicdtest.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class TestApiController {

    @PostMapping("/please")
    public ResponseEntity<?> test(){

        log.info("Please....");

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
