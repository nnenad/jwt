package com.example.myproject.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class Authentication {

    private final String key = "teste";
    private final int TEMPO_MAX_SESSAO=10;


    public String compactJws(String user) {
        String compactJws = Jwts.builder()
                .setSubject(user)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        System.out.println("compactJws = " + compactJws);

        return compactJws;
    }

    public String desCompactJws(String compactJws) {
        String desCompactJws = "";
        try{
            desCompactJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(compactJws)
                    .getBody()
                    .getSubject();

            System.out.println("desCompactJws = " + desCompactJws);

        }catch (MalformedJwtException e) {
            return "Invalid Token";
        }
        return desCompactJws;
    }
}
