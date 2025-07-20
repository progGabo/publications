package org.publications.config;

import org.publications.security.JwtFilter;
import org.publications.security.JwtTokenProvider;
import org.publications.service.impl.AuthorServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtProvider;

    public SecurityConfig(JwtTokenProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthorServiceImpl authorService
    ) throws Exception {

        JwtFilter jwtFilter = new JwtFilter(jwtProvider, authorService);

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/authors/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/publications/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/category").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/type").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/language").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/publisher").permitAll()

                        .requestMatchers(HttpMethod.POST,   "/api/publications").authenticated()
                        .requestMatchers(HttpMethod.PUT,    "/api/publications/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/publications/**").authenticated()

                        .requestMatchers("/api/auth/me").authenticated()

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider(authorService))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(AuthorServiceImpl authorService) {
        var prov = new DaoAuthenticationProvider();
        prov.setUserDetailsService(authorService);
        prov.setPasswordEncoder(passwordEncoder());
        return prov;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
