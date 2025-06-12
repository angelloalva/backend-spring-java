package com.neuromotion.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.neuromotion.backend.controller.AuthController;
import com.neuromotion.backend.model.Usuario;
import com.neuromotion.backend.repository.UsuarioRepository;
import com.neuromotion.backend.security.UsuarioDetails;

import lombok.extern.slf4j.Slf4j;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDetailsService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String compositeKey) throws UsernameNotFoundException {
        // Separar tipoDocumento y numeroDocumento
        String[] parts = compositeKey.split("\\|");
        if (parts.length != 2) {
            throw new UsernameNotFoundException("Formato de clave inválido: " + compositeKey);
        }
        
        String tipoDocumento = parts[0];
        String numeroDocumento = parts[1];
                        
     // Busca el usuario en la base de datos y extrae el Usuario del Optional
        Usuario usuario = usuarioRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + numeroDocumento));

        // Crea el objeto UsuarioDetails
        return new UsuarioDetails(usuario);
   
    }
}
