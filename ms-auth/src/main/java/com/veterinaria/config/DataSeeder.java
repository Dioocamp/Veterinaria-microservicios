package com.veterinaria.config;

import com.veterinaria.model.Rol;
import com.veterinaria.model.Usuario;
import com.veterinaria.repository.RolRepository;
import com.veterinaria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Dinko Ocampo
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        Rol admin = guardarRol("ROLE_ADMIN");
        Rol tutor = guardarRol("ROLE_TUTOR");
        Rol profesional = guardarRol("ROLE_PROFESIONAL");

        crearUsuario("admin", "admin", Set.of(admin));
        crearUsuario("tutor", "tutor", Set.of(tutor));
        crearUsuario("profesional", "profesional", Set.of(profesional));
    }

    private Rol guardarRol(String nombre) {
        Rol rol = new Rol();
        rol.setNombre(nombre);
        return rolRepository.save(rol);
    }

    private void crearUsuario(String username, String password, Set<Rol> roles) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setActivo(true);
        usuario.setRoles(roles);
        usuarioRepository.save(usuario);
    }
}
