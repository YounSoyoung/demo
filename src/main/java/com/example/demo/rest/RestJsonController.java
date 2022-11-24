package com.example.demo.rest;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/json")
@Slf4j //로깅 라이브러리
public class RestJsonController {

    /**
     * 로그 레벨
     *
     * TRACE - 잡다한 내용을 로깅(1초마다 서버가 돌아가는 기록)
     * DEBUG - 개발시 확인할 내용들 로깅
     * INFO - 운영시 필요한 정보들을 로깅
     * WARN - 에러까지는 아닌데 좀 경고 상황 로깅
     * ERROR - 심각한 에러 로깅
     *
     */
    @PostMapping("/major")
    public List<String> major(@RequestBody List<String> majorList){ //@RequestBody: JSON으로 날라온 데이터를 읽는다. //list, array: [], map, 나머지:{}
        //System.out.println("/json/major POST request!");
        //로그 찍을 때 System.out.println을 사용하면 안되는 이유)
        //1. 메모리를 많이 잡아먹는다.
        //2. 제대로 된 추적이 안된다.
        log.info("/json/major POST request! = {}", majorList); //{}을 사용하면 majorList가 중괄호 안으로 들어간다.
        return null;
    }

    @PutMapping("/bbs")
    public void bbs(@RequestBody Article article){   //json에서는 키값에 무조건 따옴표를 붙여야한다.
        log.info("/json/bbs PUT request! - {}", article);
    }

    @DeleteMapping("/bbs-list")
    public void bbsList(@RequestBody List<Article> list){
        log.info("/json/bbs-list DELETE request! - {}", list);
    }

    @Setter @Getter @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Article {
        private long id;
        private String title;
        private String content;
        private String writer;
    }

}
