package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Telefone;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.exceptions.ConflitException;
import com.javanauta.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.repository.EnderecoRepository;
import com.javanauta.usuario.infrastructure.repository.TelefoneRepository;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TelefoneRepository telefoneRepository;
    private final EnderecoRepository enderecoRepository;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    //Função que lança uma exceção caso o email exista
    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe){
                throw new ConflitException("email já cadastrado: "+email);
            }
        }
        catch (ConflitException e){
            throw new ConflitException("email já cadastrado "+e.getCause());
        }
    }

    //Função que apenas verifica se o email existe
    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email){

        try{
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email).
                            orElseThrow(() -> new ResourceNotFoundException("E-mail não encontrado: "+email))
            );

        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("E-mail não encontrado: "+email);
        }


    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO){

        //Aqui extraimos o email através do token
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        //Criptografia de senha
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO. getSenha()) : null );

        //Aqui passamos o e-mail extraído como parâmetro no findByEmail(), assim ele localiza o registro correspondente no BD
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("Email não localizado"));

        //Aqui atualizamos as informações novas do DTO (caso tenha), se não houver mudanças, ele passa o que já existe na Entity
        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO,usuarioEntity);

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }

    public TelefoneDTO atualizaTelefone(Long id, TelefoneDTO telefoneDTO){
        Telefone telefoneEntity = telefoneRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Telefone não localizado"));

        Telefone telefoneAtualizado = usuarioConverter.updateTelefone(telefoneDTO,telefoneEntity);

        telefoneRepository.save(telefoneAtualizado);

        return usuarioConverter.paraTelefoneDTO(telefoneAtualizado);

    }

    public EnderecoDTO atualizaEndereco(Long id, EnderecoDTO enderecoDTO){

        Endereco enderecoEntity = enderecoRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Telefone não localizado"));

        Endereco enderecoAtualizado = usuarioConverter.updateEndereco(enderecoEntity,enderecoDTO);

        enderecoRepository.save(enderecoAtualizado);

        return usuarioConverter.paraEnderecoDTO(enderecoAtualizado);

    }

}
