package com.neuromotion.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.neuromotion.backend.security.JwtRequestFilter;
import com.neuromotion.backend.service.UsuarioDetailsService;
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private UsuarioDetailsService usuarioDetailsService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permitir todas las peticiones OPTIONS
                .requestMatchers("/auth/**").permitAll() // endpoints públicos (login, registro)
                .requestMatchers(HttpMethod.GET, "/usuarios/**").hasAnyRole("ADMIN", "DOCTOR", "PACIENTE")
                .requestMatchers(HttpMethod.POST, "/usuarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/usuarios/**").hasRole("ADMIN")  // para editar usuarios
                 .requestMatchers(HttpMethod.GET, "/especialidades/**").hasAnyRole("ADMIN", "DOCTOR", "PACIENTE")
                  .requestMatchers(HttpMethod.POST, "/especialidades/**").hasAnyRole("ADMIN", "DOCTOR")
                  .requestMatchers(HttpMethod.GET, "/sedes/**").hasAnyRole("ADMIN", "DOCTOR", "PACIENTE")
                  .requestMatchers(HttpMethod.POST, "/sedes/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

        // Define este bean para que Spring pueda inyectar AuthenticationManager donde lo necesites
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

     // 🕵️ Define CÓMO verificar credenciales
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        // "Busca usuarios usando MI servicio personalizado"
        authProvider.setUserDetailsService(usuarioDetailsService);
        
        // "Compara contraseñas usando BCrypt"
        authProvider.setPasswordEncoder(passwordEncoder());
        
        // "Si no encuentra usuario, lanza UsernameNotFoundException"
        authProvider.setHideUserNotFoundExceptions(false);
        
        return authProvider;
    }
}