package com.example.imersaospring.repositories;

// Componente do sistema responsável por acessar o banco de dados
// Exemplo: Salvar uma venda, atualizar uma venda, deletar, buscar por id

import com.example.imersaospring.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // Linguagem JPQL que é parecida com SQL, porém ela é uma adaptação para ficar aderente ao JPA
    // Seleciona todos obj do tipo Sale onde a data estiver entre min e max ordenado pelo maior valor decrescente
    @Query("SELECT obj FROM Sale obj WHERE obj.date BETWEEN :min AND :max ORDER BY obj.amount DESC")
    Page<Sale> findSalesPage(LocalDate min, LocalDate max, Pageable pageable);
}
