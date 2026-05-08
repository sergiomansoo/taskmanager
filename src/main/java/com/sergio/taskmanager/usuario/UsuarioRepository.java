package com.sergio.taskmanager.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    public Usuario findByEmail(String email);
    public Usuario findByNome(String email);
    public boolean existsByEmail(String email);
}
