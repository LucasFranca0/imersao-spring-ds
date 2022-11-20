package com.example.imersaospring.services;

import com.example.imersaospring.entities.Sale;
import com.example.imersaospring.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

// Service é responsável por implementar operações de negócio
// Exemplo: operação para buscar as vendas

// Com a anotação @Service registra o SalesService como  um componente do sistema
@Service
public class SaleService {

    // Repository para poder acessar o banco de dados
    @Autowired
    private SaleRepository repository;

    // Método para buscar todas as vendas
    // Agora usamos o repository dentro do método para que ele consulte o banco de dados e nos retorne todos os dados
    public List<Sale> findSales() {
        return repository.findAll();
    }

    // Paginação com data mínima e data máxima
    // Os dados trafegam como texto e aqui é convertido para data
    // Depois é necessário ir para o SaleRepository para realizar a conversão
    public Page<Sale> findSalesPage(String minDate, String maxDate, Pageable pageable) {

        // Para obter a data atual é necessário usar "ofInstant"(Instant.now() para pegar o instante atual
        // ZoneId.systemDefault() pega o fuso horário do sistema
        LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

        // Se o minDate não for informado será exibido a data de um ano a atrás
        // **minus** pode ser usado para subtrair meses, semanas, dias, anos
        LocalDate min = minDate.equals("") ? today.minusYears(1) : LocalDate.parse(minDate);
        // Se o maxDate não for informado será exibido a data atual
        LocalDate max = maxDate.equals("") ? today : LocalDate.parse(maxDate);

        return repository.findSalesPage(min, max, pageable);
    }
}

