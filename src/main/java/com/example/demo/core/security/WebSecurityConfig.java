package com.example.demo.core.security;

import com.example.demo.core.security.helpers.JwtProperties;
import com.example.demo.domain.user.UserService;
import com.example.demo.domain.user.dto.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;
    private final UserMapper userMapper;

    @Autowired
    public WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder, JwtProperties jwtProperties, UserMapper userMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProperties = jwtProperties;
        this.userMapper = userMapper;
    }

    @Bean // This is the security filter chain for backend
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        requests -> requests
                                // The login and register endpoint is available to everyone with the POST Method
                                .requestMatchers(HttpMethod.POST, "/user/login", "/user/register").permitAll()
                                // The Swagger Documentation is for everyone available
                                .requestMatchers(HttpMethod.GET, "/v3/api-docs", "/v3/api-docs/swagger-config", "/swagger-ui/*").permitAll()
                                // any other request should be authenticated with the JWT token
                                .anyRequest().authenticated()
                )
                .addFilterAfter(new JWTAuthenticationFilter(new AntPathRequestMatcher("/user/login", "POST"),
                        authenticationManager(), jwtProperties, userMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JWTAuthorizationFilter(userService, jwtProperties),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean // The CORS Configuration for the backend
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", configuration);
        return configurationSource;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return new ProviderManager(provider);
    }

}
