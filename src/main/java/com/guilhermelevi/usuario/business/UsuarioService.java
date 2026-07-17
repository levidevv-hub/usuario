package com.guilhermelevi.usuario.business;


import com.guilhermelevi.usuario.business.converter.UsuarioConverter;
import com.guilhermelevi.usuario.business.dto.UsuarioDTO;
import com.guilhermelevi.usuario.infrastructure.entity.Usuario;
import com.guilhermelevi.usuario.infrastructure.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

}
