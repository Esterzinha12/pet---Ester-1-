package br.org.sesisenai.clinipet.security;


import br.org.sesisenai.clinipet.security.Users.UserJpa;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {
    public final String senhaForte = "c127a7b6adb013a5ff879ae71afa62afa4b4ceb72afaa54711dbcde67b6dc325";

    public String gerarToken(UserJpa pessoa) {
        return Jwts.builder()
                .setIssuer("CliniPet")
                .setSubject(pessoa.getPessoa().getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 2700000))
                .signWith(SignatureAlgorithm.HS256, senhaForte)
                .compact();
    }

    public void validarToken(String token) throws Exception {
        try {
            Jwts.parser().setSigningKey(senhaForte).parseClaimsJws(token);
        } catch (Exception e) {
            throw new Exception("Token Invalido 3");
        }
    }
    public String getUsuarioUsername(String token) {
        String usuario = Jwts.parser()
                .setSigningKey(senhaForte)
                .parseClaimsJws(token)
                .getBody().getSubject();
        return usuario;
    }


}
