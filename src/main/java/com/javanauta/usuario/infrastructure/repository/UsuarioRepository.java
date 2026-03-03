package com.javanauta.usuario.infrastructure.repository;


import com.javanauta.usuario.infrastructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    boolean existsByEmail(String email);

    //Serve basicamente para evitrarmos o retorno de informação nula
    Optional<Usuario> findByEmail(String email);

    //@Transactional - usado para evitar erros na hora de deletar
    @Transactional
    void deleteByEmail(String email);

}
