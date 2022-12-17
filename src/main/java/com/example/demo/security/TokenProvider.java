package com.example.demo.security;

import com.example.demo.user.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
//토클 발행하는 클래스
public class TokenProvider {

    //우리 사이트의 서명(랜덤문자열 생성(UUID 같은 거 사용) - 512바이트 이상)
    private static final String SECRET_KEY = "Q4NSl604sgyHJj1qwEkR3ycUeR4uUAt7WJraD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4H";
    //서명이 바뀌면 안되니까 final로 설정

    //토큰 발행 메서드
    public String create(UserEntity userEntity) {
        // 기한은 지금부터 1일로 설정
        Date expriyDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS) //시간 단위로도 할 수 있다.(ChronoUnit.HOURS)
        );

        // Jwt 토큰 생성
        /* 토큰의 형태
        {
            sub : 회원식별자
            iss: 토큰발행자 이름
            iot: 토큰 발행일,
            exp: 토큰만료일, (토큰이 만료되면 로그아웃이 된다.)

        } 사이트 서명
        => 사이트에 로그인을 한 애는 자동으로 위의 내용을 들고 다니게 된다
         */
        return Jwts.builder()
                //header에 들어갈 내용 및 서명을 위한 SECRET_KEY
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS512)  //인코딩
                //payload에 들어갈 내용
                .setSubject(userEntity.getId()) //sub
                .setIssuer("demo app") //iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expriyDate) // exp
                .compact();
    }

    // 토큰을 디코딩 및 파싱하고 토큰의 위조여부를 확인
    public String validateAndGetUserId(String token) {
        /*
             parseClaimsJws메서드가 Base64로 디코딩 및 파싱
             헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명한 후
             token의 서명과 비교
             위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날림
             그 중 우리는 userId가 필요하므로 getBody를 부름
         */
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))   //디코딩
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
