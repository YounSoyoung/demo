package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//백엔드 서버가 살아있는지 확인
@RestController
@Slf4j
@CrossOrigin
public class HealtyCheckController {
    @GetMapping("/")
    public String check(){
        log.info("server is running....");
        return "server is running....";
    }

    @GetMapping("/hello123")
    public String check123(){
        log.info("hello123....");
        return "안녕하세요";
    }

}
