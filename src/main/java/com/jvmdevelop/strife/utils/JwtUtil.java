package com.jvmdevelop.strife.utils;

import com.jvmdevelop.strife.model.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JwtUtil {
    @Value("${jwt.secret}")
    private static String SECRET;
    @Value("${jwt.expiration}")
    private static long EXPIRATION;
    @Value("${jwt.header}")
    private static String HEADER;
    @Value("${jwt.prefix}")
    private static String PREFIX;

    public static String generateToken(UserDetailsImpl userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET).build().parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    public String getUsernameFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET).build().parseClaimsJws(token).getBody().getSubject();
    }
}
