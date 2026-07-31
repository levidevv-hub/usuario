package com.guilhermelevi.usuario.business;


import com.guilhermelevi.usuario.business.converter.UsuarioConverter;
import com.guilhermelevi.usuario.business.dto.EnderecoDTO;
import com.guilhermelevi.usuario.business.dto.TelefoneDTO;
import com.guilhermelevi.usuario.business.dto.UsuarioDTO;
import com.guilhermelevi.usuario.infrastructure.entity.Endereco;
import com.guilhermelevi.usuario.infrastructure.entity.Telefone;
import com.guilhermelevi.usuario.infrastructure.entity.Usuario;
import com.guilhermelevi.usuario.infrastructure.exceptions.ConflitcException;
import com.guilhermelevi.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.guilhermelevi.usuario.infrastructure.repository.EnderecoRepository;
import com.guilhermelevi.usuario.infrastructure.repository.TelefoneRepository;
import com.guilhermelevi.usuario.infrastructure.repository.UsuarioRepository;
import com.guilhermelevi.usuario.infrastructure.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

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

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(
                    () -> new ConflitcException("Email não encontrado " + email)));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

   public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado " + email));

        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
   }

   public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {
       Endereco enderecoEntity = enderecoRepository.findById(idEndereco).orElseThrow(
               () -> new ResourceNotFoundException("id de endereço não encontrado "+ idEndereco));
       Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, enderecoEntity);

       return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
   }

   public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO) {
        Telefone telefoneEntity = telefoneRepository.findById(idTelefone).orElseThrow(
                () -> new ResourceNotFoundException("Id telefone não encontrado " + idTelefone));
        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, telefoneEntity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
   }

   public EnderecoDTO cadastroEndereco(String token, EnderecoDTO enderecoDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("email não encontrado " + email));
        Endereco endereco = usuarioConverter.paraEnderecoEntity(enderecoDTO, usuario.getId());
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
   }

   public TelefoneDTO cadastroTelefone(String token, TelefoneDTO telefoneDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("email não encontrado " + email));
        Telefone telefone = usuarioConverter.paraTelefoneEntity(telefoneDTO, usuario.getId());
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
   }

}
