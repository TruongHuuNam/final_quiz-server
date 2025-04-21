package com.huunam.identity_service.configuration;

import com.huunam.identity_service.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // muon phan quyen cho method nao thi chi can dat annotation tren method do
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = { "/users",
            "/auth/token",
            "/auth/introspect",
            "/auth/logout",
            "/auth/refresh",
            "/tests/**",
            "/categories/**",
            "/identity/swagger-ui.html",
            "/identity/swagger-ui/**",
            "/identity/v3/api-docs/**",
            "/identity/swagger-resources/**",
            "/identity/webjars/**"

    };

    // @Value("${jwt.signerKey}")
    // private String signerKey;

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    // @Bean: Annotations này đánh dấu rằng phương thức sẽ trả về một đối tượng và
    // đối tượng đó sẽ được Spring quản lý như một bean.
    // Bean này là một SecurityFilterChain, giúp Spring Security biết cần cấu hình
    // bảo mật cho ứng dụng.
    @Bean
    // chuỗi các filter được Spring Security sử dụng để xử lý các incoming requests
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception { // params cung cấp các phương
                                                                                         // thức để cấu hình bảo mật cho
                                                                                         // HTTP requests
        // nhung endpoints nao duoc bao ve va nhung endpoints nao cho user truy cap vao
        // ma khong can co che bao ve nao
        httpSecurity
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("*")); // Allow all origins
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    return corsConfig;
                }))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("identity/swagger-ui.html", "identity/swagger-ui/**",
                                "identity/v3/api-docs/**",
                                "identity/swagger-resources/**", "identity/webjars/**")
                        .permitAll() // cho phep tat ca user truy cap vao endpoints swagger
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        // .requestMatchers(HttpMethod.GET,"/users").hasRole(Role.ADMIN.name()) //phan
                        // quyen admin tren endpoints users
                        .anyRequest().authenticated()); // nhung request khac phai duoc authenticate moi duoc accept vao
                                                        // he thong

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())); // dang ki mot provider
                                                                               // manager(authentication) su dung
                                                                               // jwtdecoder() da config de check token
                                                                               // hop le hay khong

        httpSecurity.csrf(AbstractHttpConfigurer::disable); // Vô hiệu hóa CSRF (Cross-Site Request Forgery) cors

        return httpSecurity.build(); // cung cap lamda function
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");// k can them prefix vi da set trong build scope
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    // implement interface jwtDecoder to filterChain
    // @Bean
    // JwtDecoder jwtDecoder(){
    // //supply jwt decoder to verify
    // SecretKeySpec secretKeySpec = new
    // SecretKeySpec(signerKey.getBytes(),"HS512"); //chuyển đổi một mảng byte thô
    // thành khóa mã hóa
    // return NimbusJwtDecoder //return mot object decoder dung de giai ma va xac
    // thuc token
    // .withSecretKey(secretKeySpec)
    // .macAlgorithm(MacAlgorithm.HS512)
    // //build de co duoc bean cua jwtDecoder
    // .build(); //Phương thức build() sẽ tạo ra và trả về một đối tượng JwtDecoder,
    // // sẵn sàng sử dụng để giải mã và xác thực JWT.
    // };

    // init Bean Bcrypt password encoder
    @Bean // cho method passwordEncoder duoc goi va dua bien Bcrypt vao Application
          // Context de co the su dung o nhung noi khac
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
