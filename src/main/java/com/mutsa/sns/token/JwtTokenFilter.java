package com.mutsa.sns.token;

import com.mutsa.sns.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.split(" ")[1];
            if(jwtTokenUtil.validateToken(token)){
                //컨텍스트 생성
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                //아이디 가져오기
                String username = jwtTokenUtil.getUsername(token);

                //인증 정보 만들기
                AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        User.builder().username(username).build(),
                        token,
                        new ArrayList<>()
                );

                //컨텍스트 안에 인증정보 넣기 ( username 밖에 안들어있음 )
                securityContext.setAuthentication(authToken);
                SecurityContextHolder.setContext(securityContext);
                log.info("필터 거침");
            }
            else{
                log.info("유효하지 않은 토큰");
            }
        }
        filterChain.doFilter(request,response);
    }
}
