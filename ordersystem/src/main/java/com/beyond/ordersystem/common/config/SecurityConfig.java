package com.beyond.ordersystem.common.config;

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

@Configuration
@RequiredArgsConstructor
// PreAuthorized 어노테이션 사용하기 위한 어노테이션
// 권한이 없을 경우 filterchain에서 에러 발생
@EnableMethodSecurity // PreAuthorized 를 미리 다 가져온다. (컨트롤러 딴에서 처리하는게 아님)
public class SecurityConfig {
//    private final JwtTokenFilter jwtTokenFilter;
//    private final JwtAutenticationHandler jwtAutenticationHandler;
//    private final JwtAuthorizationHandler jwtAuthorizationHandler;

    // 내가 만든 객체는 @Component, 외부 라이브러리를 활용한 객체는 Bean+Configuration
    // Bean은 메서드 위에 붙여 Return되는 객체를 싱글톤 객체로 생성. Component는 클래스 위에 붙여 클래스 자체를 클래스 자체를 싱글톤 객체로 생성
    @Bean // 메서드가 반환하는 객체가 싱글톤
    // 필터 계층에서 필터 로직을 커스텀 (필터 레벨의 코드를 커스텀하는 코드)
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .cors(c -> c.configurationSource(corsConfiguration()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // 무조건 여기로 가서 토큰 검증(정상이면 Au객체 만듬)
//
//                .exceptionHandling(e ->
//                        e.authenticationEntryPoint(jwtAutenticationHandler) // 401의 경우
//                                .accessDeniedHandler(jwtAuthorizationHandler) // 403의 경우
//                )

                .authorizeHttpRequests(a -> a.requestMatchers("/member/create", "/member/doLogin").permitAll().anyRequest().authenticated())
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
