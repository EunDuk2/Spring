package com.beyond.basic.b2_board.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    // 내가 만든 객체는 @Component, 외부 라이브러리를 활용한 객체는 Bean+Configuration
    // Bean은 메서드 위에 붙여 Return되는 객체를 싱글톤 객체로 생성. Component는 클래스 위에 붙여 클래스 자체를 클래스 자체를 싱글톤 객체로 생성
    @Bean // 메서드가 반환하는 객체가 싱글톤
    // 필터 계층에서 필터 로직을 커스텀 (필터 레벨의 코드를 커스텀하는 코드)
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                // cors : 특정 도메인에 대한 허용 정책, postman은 cors정책에 적용X
                .cors(c -> c.configurationSource(corsConfiguration()))
                // csrf : (보안공격 중 하나로서 타 사이트의 쿠키값을 꺼내서 탈취하는 공격) -> 대비하지 않는다
                // 세션기반 로그인(MVC패턴, ssr)에서는 csrf 별도 설정하는 것이 일반적
                // 토근기반 로그인(rest api서버, csr)에서는 csrf 설정하지 않는 것이 일반적
                .csrf(AbstractHttpConfigurer::disable) // csrf 안 쓰겠따
                // http basic은 email/pw를 인코딩하여 인증하는 방식 (간단한 인증)
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션 로그인 방식 비활성화 (Stateless -> 토큰방식으로 가겠다.)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 아래 2코드는 중요
                // token을 검증하고, token검증을 통해 Authentication 객체 생성
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // 무조건 여기로 가서 토큰 검증(정상이면 Au객체 만듬)
                // 예외 api 정책 설정
                // authenticated() : 예외를 제외한 모든 요청에 대해서 Authentication객체가 생성되기를 요구 (회원가입, 로그인은 Controller로 통과)
                .authorizeHttpRequests(a -> a.requestMatchers("/author/create", "/author/doLogin").permitAll().anyRequest().authenticated())
                .build();
    }
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
