package com.neuromotion.backend.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.neuromotion.backend.service.UsuarioDetailsService;
import com.neuromotion.backend.util.JwtUtil;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
/*
 *  @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
                                    throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        String usuarioId=null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
         
                usuarioId = jwtUtil.extractUsuarioId(jwt); // <-- extrae el usuarioId

            } catch (JwtException e) {
                // Token inválido
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = usuarioDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        // Aquí agregas el usuarioId como parte de los details
                Map<String, Object> details = new HashMap<>();
                details.put("usuarioId", usuarioId);
                details.put("webDetails", new WebAuthenticationDetailsSource().buildDetails(request));
                authToken.setDetails(details);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
 */
   
 
    @Autowired
    private UsuarioDetailsService usuarioDetailsService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response, FilterChain filterChain)
                                  throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwt = null;
        String usuarioId = null;
        String tipoDocumento = null;
        String numeroDocumento = null;
        String nombre = null;

        List<String> roles = null;
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                usuarioId = jwtUtil.extractUsuarioId(jwt);
                tipoDocumento = jwtUtil.extractTipoDocumento(jwt);
                numeroDocumento = jwtUtil.extractNumeroDocumento(jwt);
                nombre = jwtUtil.extractNombre(jwt);
                roles = jwtUtil.extractRoles(jwt);
            } catch (JwtException e) {
                // Token inválido
            }
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = usuarioDetailsService.loadUserByUsername(username);
            
            if (jwtUtil.validateToken(jwt, userDetails)) {
                List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
                
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                
                // Información adicional en details
                Map<String, Object> details = new HashMap<>();
                details.put("usuarioId", usuarioId);
                details.put("tipoDocumento", tipoDocumento);
                details.put("numeroDocumento", numeroDocumento);
                details.put("nombre", nombre);
                details.put("roles", roles);
                details.put("webDetails", new WebAuthenticationDetailsSource().buildDetails(request));
                authToken.setDetails(details);
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }

}