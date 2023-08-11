package com.mutsa.sns.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private final Key signingKey;
    private final JwtParser jwtParser;

    // signature 에 시크릿 키 암호화 하여 저장
    // parser 에 signature 저장
    JwtTokenUtil(@Value("${jwt.secret}")String jwtSecret){
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();
    }

    //토큰 발급
    public String createToken(String username){
        //30분 유효 토큰
        Claims claims = Jwts.claims()
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(1800)))
                .setSubject(username);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(signingKey)
                .compact();
    }

    //토큰 유효 검증
    public boolean validateToken(String token){
        try{
            jwtParser.parseClaimsJws(token);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    //토큰의 안에 있는 Username 추출
    public String getUsername(String token){
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

}
