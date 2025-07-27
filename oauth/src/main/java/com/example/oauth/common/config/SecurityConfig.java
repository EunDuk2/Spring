package com.example.oauth.common.config;

import com.example.oauth.common.auth.JwtAutenticationHandler;
import com.example.oauth.common.auth.JwtAuthorizationHandler;
import com.example.oauth.common.auth.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAutenticationHandler jwtAutenticationHandler;
    private final JwtAuthorizationHandler jwtAuthorizationHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(c -> c.configurationSource(corsConfiguration()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // token을 검증하고, token검증을 통해 Authentication 객체 생성
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(e ->
                        e.authenticationEntryPoint(jwtAutenticationHandler) // 401의 경우
                                .accessDeniedHandler(jwtAuthorizationHandler) // 403의 경우
                )

                .authorizeHttpRequests(a -> a.requestMatchers("/member/create", "/member/doLogin", "/member/google/doLogin", "/member/kakao/doLogin").permitAll().anyRequest().authenticated())
                .build();
    }
    // Todo - 프론트 연결
    private CorsConfigurationSource corsConfiguration(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP(get, post 등) 메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더요소(Authorization 등) 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //모든 url패턴에 대해 cors설정 적용
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // 객체만들고 AuthorService에 주입
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
