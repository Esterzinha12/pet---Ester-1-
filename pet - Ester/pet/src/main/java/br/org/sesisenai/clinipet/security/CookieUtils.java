package br.org.sesisenai.clinipet.security;

import br.org.sesisenai.clinipet.security.Users.UserJpa;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;


public class CookieUtils {
    private final JwtUtils jwtUtils = new JwtUtils();

    public Cookie gerarTokenCookie(UserJpa userJpa) {
        String token = jwtUtils.gerarToken(userJpa);
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(1800);
        return cookie;
    }

    public String getTokenCookie(HttpServletRequest request) throws Exception {
        try {
            Cookie cookie = WebUtils.getCookie(request, "token");
            return cookie.getValue();
        } catch (Exception e) {
            throw new Exception("Token Invalido 1");
        }
    }

    public Cookie renovarCookie(HttpServletRequest request, String nome) {
        Cookie cookie = WebUtils.getCookie(request, nome);
        cookie.setPath("/");
        cookie.setMaxAge(1800);
        return cookie;
    }


}
