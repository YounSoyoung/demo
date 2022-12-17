package com.example.demo.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component //Controller도 아니고 기타 등등 빈을 등록할 때는 Component
@Slf4j
@RequiredArgsConstructor //TokenProvider를 주입받기 위해 사용
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    //컨트롤러 들어오기 전 장벽: 필터
    //필터를 거친 다음 컨트롤러를 실행
    //필터를 여러개 사용할 수 있기 때문에 FilterChain을 넣어 이 필터를 몇 번째 필터로 사용할 것인지 지정한다
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // 요청에서 토큰 가져오기
            String token = parseBearerToken(request);
            log.info("Token Filter is running.... - token: {}", token);

            // 토큰 검사하기
            if (token != null && !token.equalsIgnoreCase("null")) {
                // userId가져오기. 위조된 경우 예외가 발생한다.
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("인증된 user id : {}", userId);

                // 인증 완료!! api서버에서는 SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, //@AuthenticationPrincipa를 통해 TodoApiController로 userId가 넘어간다.
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);

                SecurityContextHolder.setContext(securityContext);
            }

        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
        }

        // 필터체인에 연결한다.
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // Http요청 헤더에서 Bearer 토큰을 가져온다
        //Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzZDdkMzI2NC0yYWQ5LTRkZWYtOTJhMi00ZTIxNzk3YjIyNDAiLCJpc3MiOiJkZW1vIGFwcCIsImlhdCI6MTY3MTI0NTc0NSwiZXhwIjoxNjcxMzMyMTQ1fQ.n4sQh8C62owh_H8M825_GwCWa6Bk8KyzPEHBxXJ1GUiVED5ppqQSEAlCY6JtVMlUDpNrDBm6DmDkeokpAd1Z4w
        //Bearer이라는 접두사를 붙인 다음 token값을 가져온다.
        String bearerToken = request.getHeader("Authorization");

        // 토큰값을 파싱하여 리턴
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); //토큰값은 Bearer 뒤에 있기 때문에 Bearer를 자른 다음 뒤에 있는 토큰값만 추출한다.
        }
        return null;

    }
}
