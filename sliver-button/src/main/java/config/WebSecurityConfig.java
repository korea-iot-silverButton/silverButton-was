package config;

import filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class WebSecurityConfig {

    @Lazy // 지연 로딩: 의존성 주입 시점이 필터가 사용될 때 로드됨
    @Autowired
    /*
     * jwtAuthenticationFilter (JWT 인증 필터)
     *
     * * 요청이 들어올 때 JWT 토큰을 검증하는 필터 - 검증하여 사용자를 인증
     * : UsernamePasswordAuthenticationFilter 이전에 동작, JWT 토큰이 유효한지 검사하여 사용자를 인증
     * */
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // 주소체계를 확인해보는 시스템


        CorsConfiguration config = new CorsConfiguration();

        // ✅ 중요한 부분임 !! 꼭 복습하기
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 설정 적용


        return new CorsFilter(source);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF 보호 비활성화 (REST API에서는 보통 비활성화)
                .csrf(AbstractHttpConfigurer::disable)
                // CORS 정책 활성화
                .cors(withDefaults())

                // 요청 인증 및 권한 부여 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/v1/user/**")
                        )

                        .permitAll()

                        .anyRequest().authenticated())


                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(BCryptPasswordEncoder bCryptpasswordEncoder) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setPasswordEncoder(bCryptpasswordEncoder);

        return new ProviderManager(List.of(authProvider));

    }
    @Bean
    public BCryptPasswordEncoder bCryptpasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
