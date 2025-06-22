package com.neuromotion.backend.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.neuromotion.backend.enums.Rol;

@Component
public class RoleUtil {
    
public static Set<Rol> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof Map) {
            Map<String, Object> details = (Map<String, Object>) authentication.getDetails();
            Object rolesObj = details.get("roles");

            if (rolesObj instanceof List<?>) {
                List<String> rolesList = (List<String>) rolesObj;

                Set<Rol> rolesSet = rolesList.stream()
                        .map(Rol::valueOf) // Convierte cada String en el Enum correspondiente
                        .collect(Collectors.toSet());

                return rolesSet;
            }
        }
        return new HashSet<>();
    }
    
    public static List<String> getCurrentUserRoleNames() {
        return getCurrentUserRoles().stream()
            .map(Rol::name)
            .collect(Collectors.toList());
    }
    
    public static boolean hasRole(String roleName) {
        return getCurrentUserRoles().stream()
            .anyMatch(rol -> rol.name().equals(roleName));
    }
    
    public static boolean hasRole(Rol role) {
        return getCurrentUserRoles().contains(role);
    }
    
    public static boolean hasAnyRole(String... roleNames) {
        Set<String> userRoleNames = getCurrentUserRoles().stream()
            .map(Rol::name)
            .collect(Collectors.toSet());
        return Arrays.stream(roleNames)
            .anyMatch(userRoleNames::contains);
    }
    
    public static boolean hasAnyRole(Rol... roles) {
        Set<Rol> userRoles = getCurrentUserRoles();
        return Arrays.stream(roles)
            .anyMatch(userRoles::contains);
    }
    
    public static boolean hasAllRoles(String... roleNames) {
        Set<String> userRoleNames = getCurrentUserRoles().stream()
            .map(Rol::name)
            .collect(Collectors.toSet());
        return Arrays.stream(roleNames)
            .allMatch(userRoleNames::contains);
    }
    
    public static boolean hasAllRoles(Rol... roles) {
        Set<Rol> userRoles = getCurrentUserRoles();
        return Arrays.stream(roles)
            .allMatch(userRoles::contains);
    }
    
    public static boolean isAdmin() {
        return hasRole(Rol.ADMIN);
    }
    
    public static boolean isDoctor() {
        return hasRole(Rol.DOCTOR);
    }
    
    public static boolean isPaciente() {
        return hasRole(Rol.PACIENTE);
    }
    
    public static Rol getPrimaryRole() {
        Set<Rol> roles = getCurrentUserRoles();
        
        // Jerarquía de roles: ADMIN > DOCTOR > PACIENTE
        if (roles.contains(Rol.ADMIN)) return Rol.ADMIN;
        if (roles.contains(Rol.DOCTOR)) return Rol.DOCTOR;
        if (roles.contains(Rol.PACIENTE)) return Rol.PACIENTE;
        return null;
    }
    
    public static boolean canAccessPatientData() {
        return hasAnyRole(Rol.ADMIN, Rol.DOCTOR);
    }
    
    public static boolean canManageUsers() {
        return hasRole(Rol.ADMIN);
    }
    
    public static boolean canManageAppointments() {
        return hasAnyRole(Rol.ADMIN, Rol.DOCTOR);
    }
    
    public static boolean canAccessUserData(String targetUsuarioId) {
        String currentUserId = AuthenticationUtil.getCurrentUsuarioId();
        
        if (isAdmin()) {
            return true;
        }
        
        if (isDoctor()) {
            return true; // Lógica específica según tu negocio
        }
        
        if (isPaciente()) {
            return currentUserId != null && currentUserId.equals(targetUsuarioId);
        }
        
        return false;
    }
    
    public static Optional<Rol> getRoleByName(String roleName) {
        return getCurrentUserRoles().stream()
            .filter(rol -> rol.name().equals(roleName))
            .findFirst();
    }
    
    public static int getRoleCount() {
        return getCurrentUserRoles().size();
    }
    
    public static boolean hasMultipleRoles() {
        return getRoleCount() > 1;
    }
    
    // Método para debugging
    public static String getRolesSummary() {
        Set<Rol> roles = getCurrentUserRoles();
        if (roles.isEmpty()) {
            return "Sin roles asignados";
        }
        
        return roles.stream()
            .map(Rol::name)
            .collect(Collectors.joining(", "));
    }
    
    // Métodos de utilidad adicionales para trabajar con enums
    public static List<Rol> getAllRoles() {
        return Arrays.asList(Rol.values());
    }
    
    public static boolean isValidRole(String roleName) {
        try {
            Rol.valueOf(roleName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    public static Optional<Rol> parseRole(String roleName) {
        try {
            return Optional.of(Rol.valueOf(roleName));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}