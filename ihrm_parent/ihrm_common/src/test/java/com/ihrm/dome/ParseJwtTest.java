package com.ihrm.dome;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.net.SocketTimeoutException;

public class ParseJwtTest {
    /**
     * 解析jwt字符串
     * @param args
     */
    public static void main(String[] args) {
        String token ="\n"+"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE1NzczNTMyNTQsImNvbXBhbnlJZCI6IjEyMzQ1Njc4OSIsImNvbXBhbnlOYW1lIjoi5LqR5ZKM5pWw5o2uIn0.4-qLiSaKISBmAzj9H-T1iqdztOI-CryswEDaT_m0BMg";
        Claims claims = Jwts.parser().setSigningKey("ihrm123").parseClaimsJws(token).getBody();
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
        //解析自定义的claim内容
        String comapnyId =(String) claims.get("companyId");
        String comapnyName =(String) claims.get("companyName");
        System.out.println(comapnyId+comapnyName);



    }
}
