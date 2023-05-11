package br.org.sesisenai.clinipet.security;
import br.org.sesisenai.clinipet.security.service.JpaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class AutenticacaoFiltro extends OncePerRequestFilter {

    private final CookieUtils cookieUtils = new CookieUtils();
    private final JwtUtils jwtUtils = new JwtUtils();
    private JpaService jpaService;



    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = cookieUtils.getTokenCookie(request);
            System.out.println(token);
            jwtUtils.validarToken(token);

           String usuarioUsername = jwtUtils.getUsuarioUsername(token);
           System.out.println(usuarioUsername);
           UserDetails user = jpaService.loadUserByUsername(usuarioUsername);




            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(),
                            user.getPassword(), user.getAuthorities());
            System.out.println(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(
                    usernamePasswordAuthenticationToken);


            System.out.println("valido ");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                validarUrl(request.getRequestURI());
            } catch (Exception e2) {
                e2.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } finally {
            System.out.println("Filtro finalizado");
            filterChain.doFilter(request, response);
        }
    }

    private void validarUrl(String url) throws Exception {
        if (!(url.equals("/api/login/auth")
                || url.equals("/api/logout")
                || url.equals("/api/login")
                || url.equals("/login")
                || url.equals("/logout")
                || url.equals("/api/veterianrio")
                || url.equals("/api/servico")
        )) {
            System.out.println("URL não permitida: " + url);
            throw new Exception("Url não permitida");
        }
        System.out.println("URL permitida: " + url);
    }
}
