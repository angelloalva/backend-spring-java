
package com.neuromotion.backend.util;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
    
    public static String getCurrentUsuarioId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (String) details.get("usuarioId");
        }
        return null;
    }
    
    public static String getCurrentTipoDocumento() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (String) details.get("tipoDocumento");
        }
        return null;
    }
    
    public static String getCurrentNumeroDocumento() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (String) details.get("numeroDocumento");
        }
        return null;
    }
    
    public static String getCurrentNombre() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (String) details.get("nombre");
        }
        return null;
    }
        public static String getCurrentApellidos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (String) details.get("apellidos");
        }
        return null;
    }
            public static String getCurrentDireccion() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (String) details.get("direccion");
        }
        return null;
    }
            public static String getCurrentCorreo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (String) details.get("correo");
        }
        return null;
    }
               public static String getCurrentCelular() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            return (String) details.get("celular");
        }
        return null;
    }
    public static String getCurrentDocumentoCompleto() {
        String tipo = getCurrentTipoDocumento();
        String numero = getCurrentNumeroDocumento();
        return (tipo != null && numero != null) ? tipo + "-" + numero : null;
    }
}