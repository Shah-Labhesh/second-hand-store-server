package com.labhesh.secondhandstore.config;

import com.labhesh.secondhandstore.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

        private final JwtRequestFilter filter;

        @Bean
        public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(AbstractHttpConfigurer::disable)
                                .cors(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(
                                                                new AntPathRequestMatcher("/"),
                                                                new AntPathRequestMatcher("/swagger-ui/index.html"),
                                                                new AntPathRequestMatcher("/context-path/**"),
                                                                new AntPathRequestMatcher("/swagger-ui/**"),
                                                                new AntPathRequestMatcher("/docs"),
                                                                new AntPathRequestMatcher("/docs.yaml"),
                                                                new AntPathRequestMatcher("/swagger-ui.html"),
                                                                new AntPathRequestMatcher("/api/auth/**"),
                                                                new AntPathRequestMatcher("/api/image/**"),
                                                                // new AntPathRequestMatcher("/verify/**"),
                                                                new AntPathRequestMatcher("/api/item/**"),
                                                                new AntPathRequestMatcher("/api/item/review/**"),
                                                                new AntPathRequestMatcher("/chat/**"))
                                                .permitAll()
                                                .requestMatchers(
                                                new AntPathRequestMatcher("/api/cart/**"),
                                                new AntPathRequestMatcher("/api/order/**"),
                                                new AntPathRequestMatcher("/api/item/review", HttpMethod.POST.name())
                                                ).hasAuthority(
                                                UserRole.USER.name()
                                                )
                                                .requestMatchers(
                                                                new AntPathRequestMatcher("/api/admin/**"))
                                                .hasAuthority(UserRole.ADMIN.name())
                                                .anyRequest().authenticated())
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                                .sessionManagement(
                                                sessionManagement -> sessionManagement
                                                                .sessionCreationPolicy(
                                                                                SessionCreationPolicy.STATELESS));
                http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}