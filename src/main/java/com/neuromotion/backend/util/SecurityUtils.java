package com.neuromotion.backend.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.neuromotion.backend.enums.Rol;
import com.neuromotion.backend.security.UsuarioDetails;
@Component
public class SecurityUtils {
    public static Set<Rol> getRolesFromAuthentication(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
             .map(nombre -> {
                if (nombre.startsWith("ROLE_")) {
                    nombre = nombre.substring(5); // elimina "ROLE_"
                }
                return Rol.valueOf(nombre);
            })
            .collect(Collectors.toSet());
    }

     public String extraerDocumentoDeAuth(Authentication authentication) {
        String username = ((UsuarioDetails) authentication.getPrincipal()).getUsername();
        String[] parts = username.split("\\|");
        if (parts.length < 2) {
            throw new IllegalStateException("Formato de username inválido: " + username);
        }
        return parts[1];
    }
    
    public boolean esAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }
}