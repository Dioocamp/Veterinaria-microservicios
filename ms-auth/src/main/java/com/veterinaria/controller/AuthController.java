package com.veterinaria.controller;

import com.veterinaria.dto.LoginRequest;
import com.veterinaria.dto.TokenResponse;
import com.veterinaria.model.Usuario;
import com.veterinaria.repository.UsuarioRepository;
import com.veterinaria.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author Dinko Ocampo
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> oUsuario = usuarioRepository.findByUsername(request.getUsername());

        if (oUsuario.isPresent() && oUsuario.get().getPassword().equals(request.getPassword())) {
            String token = jwtService.generateToken(oUsuario.get());
            return ResponseEntity.ok(new TokenResponse(token, "Bearer", oUsuario.get().getUsername()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
    }
}
