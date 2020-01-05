package com.ihrm.dome;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJwtTest {
    /**
     * 通过jwt创建token字符串
     * @param args
     */
    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder().setId("888").setSubject("小白")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,"ihrm")
                .claim("companyId","123456789")
                .claim("companyName","云和数据");
        String token = jwtBuilder.compact();
        System.out.println(token);

    }
}
