package com.neuromotion.backend.controller;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neuromotion.backend.dto.AuthResponseDTO;
import com.neuromotion.backend.dto.ErrorResponseDTO;
import com.neuromotion.backend.dto.LoginRequest;
import com.neuromotion.backend.dto.RegisterResponseDTO;
import com.neuromotion.backend.dto.RegistroRequest;
import com.neuromotion.backend.enums.Rol;
import com.neuromotion.backend.enums.TipoDocumento;
import com.neuromotion.backend.exceptions.CustomAuthException;
import com.neuromotion.backend.model.Usuario;
import com.neuromotion.backend.repository.UsuarioRepository;
import com.neuromotion.backend.service.AuthService;
import com.neuromotion.backend.service.UsuarioDetailsService;
import com.neuromotion.backend.util.JwtUtil;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest authRequest) {
        try {
            AuthResponseDTO response = authService.login(authRequest);
            return ResponseEntity.ok(response);
        } catch (CustomAuthException e) {
            logger.warn("Error en login: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus())
                    .body(new ErrorResponseDTO(e.getMessage(), e.getStatus()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistroRequest request) {
        try {
            RegisterResponseDTO response = authService.register(request);
            return ResponseEntity.status(201).body(response);
        } catch (CustomAuthException e) {
            logger.warn("Error en registro: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus())
                    .body(new ErrorResponseDTO(e.getMessage(), e.getStatus()));
        }
    }
     @PostMapping("/create")
    public ResponseEntity<String> createTestUser() {
        try {
            Usuario usuario = new Usuario();
            usuario.setTipoDocumento("1");
            usuario.setNumeroDocumento("12345678");
            usuario.setPassword(passwordEncoder.encode("123456"));
            usuario.setCorreo("test@email.com");
            usuario.setNombres("Usuario Test");
            usuario.setApellidos("Test");
            usuario.setRoles(Set.of(Rol.ADMIN));
            
            usuarioRepository.save(usuario);
            
            return ResponseEntity.ok("Usuario creado: 1|123456 con contraseña: 123456");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

