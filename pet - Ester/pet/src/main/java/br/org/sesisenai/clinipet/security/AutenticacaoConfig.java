package br.org.sesisenai.clinipet.security;

import br.org.sesisenai.clinipet.security.service.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class AutenticacaoConfig {

    @Autowired JpaService jpaService;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jpaService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .requestMatchers(  "/login", "/logout").permitAll()

                .requestMatchers(HttpMethod.GET, "/cliente/*","/cliente", "/agenda/*","/agenda","/prontuario/*","/prontuario").hasAnyAuthority("VETERINARIO", "ATENDENTE", "CLIENTE")

                .requestMatchers(HttpMethod.GET, "/animal/*", "/animal", "/atendente/*","/atendente").hasAnyAuthority("VETERINARIO", "ATENDENTE")

                .requestMatchers(HttpMethod.POST, "/animal", "/cliente", "/agenda").hasAnyAuthority("ATENDENTE")

                .requestMatchers(HttpMethod.POST, "/prontuario", "/servico", "/veterinario", "/atendente").hasAnyAuthority("VETERINARIO")

                .requestMatchers(HttpMethod.DELETE, "/animal/*", "/cliente/*", "/agenda/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")

                .requestMatchers(HttpMethod.DELETE, "/prontuario/*", "/servico/*", "/veterinario/*", "/atendente/*").hasAnyAuthority("VETERINARIO")

                .requestMatchers(HttpMethod.PUT, "/animal/*", "/cliente/*", "/agenda/*").hasAnyAuthority("ATENDENTE", "VETERINARIO")

                .requestMatchers(HttpMethod.PUT, "/prontuario/*", "/servico/*", "/veterinario/*", "/atendente/*").hasAnyAuthority("VETERINARIO")


                .requestMatchers(HttpMethod.GET, "/veterinario/*","/veterinario", "/servico/*", "/servico").permitAll()
                .anyRequest().authenticated();

        httpSecurity.csrf().disable()
                .cors().configurationSource(corsConfigurationSource());
        httpSecurity.logout().deleteCookies("token").permitAll();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(new AutenticacaoFiltro(jpaService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


}
