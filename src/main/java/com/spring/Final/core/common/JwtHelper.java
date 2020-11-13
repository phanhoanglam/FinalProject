package com.spring.Final.core.common;

import com.spring.Final.core.exceptions.TokenInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class JwtHelper {

    private static final String secret = "ahsvdhjavdjhasdj14bjh12b3eq";

    public static HashMap<String, Object> validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JwtHelper.secret)
                    .parseClaimsJws(token)
                    .getBody();
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("id", Integer.parseInt(claims.getId()));
            data.put("subject", claims.getSubject());

            //TODO check token Expiration
            //Date expDate = claims.getExpiration();
            //If(currentDate < expDate) => token het han


            return data;
        } catch (Exception ex) {
            throw new TokenInvalidException();
        }
    }

    public static String generateToken(Integer id, String subject) {
        Date expiredAt = DateUtils.addHours(new Date(), 168); // 24 hours
        JwtBuilder builder = Jwts.builder()
                .setId(id.toString())
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expiredAt)
                .signWith(SignatureAlgorithm.HS256, JwtHelper.secret);
        String jwt = builder.compact();

        return jwt;
    }

    public static String generateBearerToken(Integer id, String subject) {
        return "Bearer " + JwtHelper.generateToken(id, subject);
    }
}
