/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.alunos.felipe.restful.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;

/**
 *
 * @author felipe
 */
public class JwtTools {
    private static JwtTools jwtTools = null;
    private static final long EXPIRATION_LIMIT = 30; // in minutes
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    private JwtTools() { }
    
    public static JwtTools getInstance() {
        if (jwtTools == null)
            jwtTools = new JwtTools();
        return jwtTools;
    }
    
    public String generateKey(String username, String password) {
        return Jwts
                .builder()
                .setSubject(username)
                .setSubject(password)
                .signWith(key)
                .setExpiration(getExpirationDate())
                .compact();
                
    }
    
    public void verifyKey(String privateKey) throws ExpiredJwtException, MalformedJwtException {
        Jwts.parser().setSigningKey(key).parseClaimsJws(privateKey);
    }
    
    private Date getExpirationDate() {
        long currentTimeMillis = System.currentTimeMillis();
        long expMilliSeconds = TimeUnit.MINUTES.toMillis(EXPIRATION_LIMIT);
        return new Date(currentTimeMillis + expMilliSeconds);
    }
}
