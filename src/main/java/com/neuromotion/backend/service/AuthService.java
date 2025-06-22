package com.neuromotion.backend.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.neuromotion.backend.dto.AuthResponseDTO;
import com.neuromotion.backend.dto.LoginRequest;
import com.neuromotion.backend.dto.PerfilResponseDTO;
import com.neuromotion.backend.dto.RegisterResponseDTO;
import com.neuromotion.backend.dto.RegistroRequest;
import com.neuromotion.backend.dto.UsuarioRequest;
import com.neuromotion.backend.dto.UsuarioResponse;
import com.neuromotion.backend.enums.Rol;
import com.neuromotion.backend.enums.TipoDocumento;
import com.neuromotion.backend.exceptions.CustomAuthException;
import com.neuromotion.backend.model.Usuario;
import com.neuromotion.backend.repository.UsuarioRepository;
import com.neuromotion.backend.security.CustomUserDetails;
import com.neuromotion.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthResponseDTO login(LoginRequest request) {
        try {
            logger.info("Intento de login: tipo={}, numero={}",
                    request.getTipoDocumento(), request.getNumeroDocumento());

            /*String compositeKey = request.getTipoDocumento() + "|" + request.getNumeroDocumento();

            // Autenticar al usuario
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(compositeKey, request.getPassword())
            );

           

            // Buscar el usuario en el repositorio
            Usuario usuario = usuarioRepository.findByTipoDocumentoAndNumeroDocumento(
                    request.getTipoDocumento(), request.getNumeroDocumento())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Generar token JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(compositeKey);
            String jwt = jwtUtil.generateToken(userDetails, usuario.getId());
            // Mapear datos del usuario a DTO
            UsuarioResponse usuarioDTO = new UsuarioResponse(
                    usuario.getId(),
                    usuario.getNombres(),
                    usuario.getApellidos(),
                    usuario.getCelular(),
                    usuario.getCorreo(),
                    usuario.getDireccion(),
                    usuario.getTipoDocumento(),
                    usuario.getNumeroDocumento(),
                    usuario.getRoles()
            );*/

             // Autenticar con username compuesto
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(), // tipoDocumento-numeroDocumento
                    request.getPassword()
                )
            );
            
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService
                .loadUserByUsername(request.getUsername());
            
            String jwt = jwtUtil.generateToken(userDetails);
    
            UsuarioResponse usuarioDTO = new UsuarioResponse(userDetails.getUsuarioId()
                , userDetails.getTipoDocumento()
                , userDetails.getNumeroDocumento()
                , userDetails.getNombre()
                , userDetails.getRoles()
            );

            logger.info("Login exitoso para {} {}", request.getTipoDocumento(), request.getNumeroDocumento());
            return new AuthResponseDTO(jwt, usuarioDTO);

        } catch (BadCredentialsException e) {
            logger.warn("Credenciales inválidas: tipo={}, numero={}",
                    request.getTipoDocumento(), request.getNumeroDocumento());
            throw new CustomAuthException("Credenciales inválidas", 401);
        } catch (Exception e) {
            logger.error("Error en autenticación", e);
            throw new CustomAuthException("Error en autenticación: " + e.getMessage(), 500);
        }
    }

     public PerfilResponseDTO obtenerPerfilActual(String userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return new PerfilResponseDTO(
                usuario.getId(),
                usuario.getTipoDocumento(),
                usuario.getNumeroDocumento(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getCelular(),
                usuario.getCorreo(),
                usuario.getDireccion(),
                usuario.getRoles()
        );
    }

    public RegisterResponseDTO register(RegistroRequest request) {
        // Validar tipo de documento
        if (!TipoDocumento.isValid(request.getDocumentoTipo())) {
            throw new CustomAuthException("Tipo de documento inválido. Solo se permiten: 1-DNI, 2-PASAPORTE, 3-CARNET.", 400);
        }

        // Verificar si el usuario ya existe
        if (usuarioRepository.findByTipoDocumentoAndNumeroDocumento(
                request.getDocumentoTipo(), request.getDocumentoNumero()).isPresent()) {
            throw new CustomAuthException("Usuario ya existe", 409);
        }

        // Validar contraseña
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new CustomAuthException("La contraseña es obligatoria", 400);
        }

        // Crear nuevo usuario
        Usuario nuevo = new Usuario();
      
        nuevo.setTipoDocumento(request.getDocumentoTipo());
        nuevo.setNumeroDocumento(request.getDocumentoNumero());
        nuevo.setNombres(request.getNombres());
        nuevo.setApellidos(request.getApellidos());
        nuevo.setCorreo(request.getCorreo());
        nuevo.setCelular(request.getCelular());
        nuevo.setDireccion(request.getDireccion());
        nuevo.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevo.setRoles(Set.of(Rol.PACIENTE));

        // Guardar usuario
        usuarioRepository.save(nuevo);

        // Mapear datos del usuario a DTO
        UsuarioRequest usuarioDTO = new UsuarioRequest(
                nuevo.getId(),nuevo.getTipoDocumento(),
                nuevo.getNumeroDocumento(),
                nuevo.getNombres(),
                nuevo.getApellidos(),
                nuevo.getCelular(),
                nuevo.getCorreo(),
                nuevo.getDireccion()
                ,nuevo.getRoles()
        );

        logger.info("Usuario registrado correctamente: {}", request.getDocumentoNumero());
        return new RegisterResponseDTO("Usuario registrado con éxito", usuarioDTO);
    }
}
