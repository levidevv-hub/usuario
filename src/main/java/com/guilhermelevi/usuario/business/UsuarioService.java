package com.guilhermelevi.usuario.business;


import com.guilhermelevi.usuario.business.converter.UsuarioConverter;
import com.guilhermelevi.usuario.business.dto.UsuarioDTO;
import com.guilhermelevi.usuario.infrastructure.entity.Usuario;
import com.guilhermelevi.usuario.infrastructure.exceptions.ConflitcException;
import com.guilhermelevi.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.guilhermelevi.usuario.infrastructure.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificarEmailExistente(email);
            if (existe) {
                throw new ConflitcException("Email ja cadastrado" + email);
            }
        } catch (ConflitcException e) {
            throw new ConflitcException("Email ja cadastrado" + e.getCause());
        }
    }

    public boolean verificarEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ConflitcException("Email não encontrado " + email)
        );
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }
}
