// package com.mc.security.Configurations;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;


// @Configuration
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//         httpSecurity
//                 .authorizeHttpRequests(authorize -> authorize
//                         .requestMatchers(HttpMethod.GET, "/loginSuccess").permitAll() // Permitir acceso sin autenticación
//                         .requestMatchers(HttpMethod.GET, "/loginFailure").permitAll() // Permitir acceso a la ruta de fallo de login
//                         .requestMatchers(HttpMethod.GET, "/**").permitAll() // Permitir acceso a todas las rutas GET
//                         .requestMatchers(HttpMethod.POST, "/**").permitAll() // Permitir acceso a todas las rutas POST
//                         .anyRequest().authenticated() // Todas las demás solicitudes requieren autenticación
//                 )
//                 .csrf(csrf -> csrf.disable())
//                 .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/loginSuccess", true));

//         return httpSecurity.build();
//     }

// }

