package com.neuromotion.backend.controller;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.neuromotion.backend.dto.RegistroRequest;
import com.neuromotion.backend.dto.UsuarioPasswordChangeRequest;
import com.neuromotion.backend.dto.UsuarioUpdateRequest;
import com.neuromotion.backend.model.Usuario;
import com.neuromotion.backend.repository.UsuarioRepository;
import com.neuromotion.backend.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PACIENTE')")
    public ResponseEntity<?> obtenerUsuario(@PathVariable String id, Authentication authentication) {
       return usuarioService.obtenerUsuario(id, authentication);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PACIENTE')")
    public ResponseEntity<?> actualizarUsuario(@PathVariable String id,
                                               @Valid @RequestBody UsuarioUpdateRequest updateRequest,
                                               Authentication authentication) {
      return usuarioService.actualizarUsuario(id, updateRequest, authentication);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String id) {
       return usuarioService.eliminarUsuario(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody RegistroRequest usuarioDto, Authentication authentication) {
       return usuarioService.crearUsuario(usuarioDto, authentication);
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PACIENTE')")
    public ResponseEntity<?> cambiarPassword(@PathVariable String id,
                                            @Valid @RequestBody UsuarioPasswordChangeRequest request,
                                         Authentication authentication) {
     return usuarioService.cambiarPassword(id, request, authentication);
}
  
}