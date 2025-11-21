// repository/PedidoRepository.java
package com.papeleria.repository;

import com.papeleria.model.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
    List<Pedido> findAllByOrderByFechaDesc();
}