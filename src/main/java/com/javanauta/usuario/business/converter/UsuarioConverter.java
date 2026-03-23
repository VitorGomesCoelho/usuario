package com.javanauta.usuario.business.converter;

import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Telefone;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioConverter {

    // -----------------------------
    //DTO para  Entity
    public Usuario paraUsuario(UsuarioDTO usuarioDTO){
        return  Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEndereco(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefone(usuarioDTO.getTelefones()))
                .build();
    }

    public List<Endereco> paraListaEndereco (List<EnderecoDTO> enderecoDTOS){
        return enderecoDTOS.stream().map(this::paraEndereco).toList();

    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .cep(enderecoDTO.getCep())
                .build();
    }

    public List<Telefone> paraListaTelefone (List<TelefoneDTO> telefoneDTOS){

        List<Telefone> telefoneList = new ArrayList<>();
        for (TelefoneDTO telefoneDTO : telefoneDTOS){
            telefoneList.add(paraTelefone(telefoneDTO));
        }
        return telefoneList;
    }

    public Telefone paraTelefone (TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .ddd(telefoneDTO.getDdd())
                .numero(telefoneDTO.getNumero())
                .build();
    }

    // -----------------------------
    //Entity para DTO
    public UsuarioDTO paraUsuarioDTO(Usuario usuario){
        return  UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(paraListaEnderecoDTO(usuario.getEnderecos()))
                .telefones(paraListaTelefoneDTO(usuario.getTelefones()))
                .build();
    }

    public List<EnderecoDTO> paraListaEnderecoDTO (List<Endereco> endereco){
        return endereco.stream().map(this::paraEnderecoDTO).toList();

    }

    public EnderecoDTO paraEnderecoDTO(Endereco endereco){
        return EnderecoDTO.builder()
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .build();
    }

    public List<TelefoneDTO> paraListaTelefoneDTO (List<Telefone> telefone){

        List<TelefoneDTO> telefoneList = new ArrayList<>();
        for (Telefone telefones : telefone){
            telefoneList.add(paraTelefoneDTO(telefones));
        }
        return telefoneList;
    }

    public TelefoneDTO paraTelefoneDTO (Telefone telefone){
        return TelefoneDTO.builder()
                .ddd(telefone.getDdd())
                .numero(telefone.getNumero())
                .build();
    }

    // -----------------------------
    //Demais


    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario usuarioEntity){
        return Usuario.builder()
                .nome(usuarioDTO.getNome() != null? usuarioDTO.getNome() : usuarioEntity.getNome())
                .id(usuarioEntity.getId())
                .senha(usuarioDTO.getSenha() != null? usuarioDTO.getSenha() : usuarioEntity.getSenha())
                .email(usuarioDTO.getEmail() != null? usuarioDTO.getEmail() : usuarioEntity.getEmail())
                .enderecos(usuarioEntity.getEnderecos())
                .telefones(usuarioEntity.getTelefones())
                .build();
    }


}
