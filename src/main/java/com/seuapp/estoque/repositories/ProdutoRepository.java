package com.seuapp.estoque.repositories;

import com.seuapp.estoque.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String> {
    // JpaRepository já fornece métodos CRUD básicos, como findAll(), save(), deleteById(), etc.
}
