// repository/UsuarioRepository.java
package com.papeleria.repository;

import com.papeleria.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    void deleteByCodigo(String codigo);
}