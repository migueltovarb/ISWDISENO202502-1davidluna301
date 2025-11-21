// repository/ProductoRepository.java
package com.papeleria.repository;

import com.papeleria.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends MongoRepository<Producto, String> {
    Optional<Producto> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<Producto> findByActivoTrue();
    List<Producto> findByDetalleContainingIgnoreCaseAndActivoTrue(String detalle);
    List<Producto> findByCodigoContainingAndActivoTrue(String codigo);
}