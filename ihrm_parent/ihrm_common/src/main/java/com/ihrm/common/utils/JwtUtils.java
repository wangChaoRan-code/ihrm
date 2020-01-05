package com.ihrm.common.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class JwtUtils {
    //签名密匙
    private String key;
    //签名失效时间
    private Long ttl;

    /**
     * 设置认证token
     * id为登录用户的id
     * subject为登录的用户名
     *
     *
     */
    public String createJwt(String id, String name, Map<String,Object> map) {
        //设置失效时间
        Long now = System.currentTimeMillis();//当前毫秒
        Long exp = ttl + now;
        //创建JwtBuilder
        JwtBuilder jwtBuilder = Jwts.builder().setId(id).setSubject(name)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key);
        //根据Mpa设置claims
        jwtBuilder.setClaims(map);
        jwtBuilder.setExpiration(new Date(exp));
        //创建token
        String token = jwtBuilder.compact();
        return token;
    }
    /**
     * 解析token
     *
     */
    public Claims parseJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }
}
