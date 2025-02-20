//package com.aggregator.quoteAggregator.security;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/**","/h2-console/**").permitAll()  // Allow all requests without authentication
//                );
//        return http.build();
//    }
//}
