package com.acrevu.acrevu_backend.security;

import com.acrevu.acrevu_backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService{

    private String secretKey = "";

    JWTService(){
        try {
            KeyGenerator key = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk= key.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("mobile", user.getMobileNumber());
        claims.put("role", user.getRole().name());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] sk = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(sk);
    }

    public String getUserName(String token) {
        //extract the username from token

        return extractClaim(token , Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        System.out.println(claims);

        return  claimResolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {

        final String userName = getUserName(token);
        System.out.println(userName);
        System.out.println(userDetails.getUsername());
        System.out.println(userName.equals(userDetails.getUsername()) && !isTokenExpired(token));

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }


}