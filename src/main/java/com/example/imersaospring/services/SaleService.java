package com.example.imersaospring.services;

import com.example.imersaospring.entities.SaleModel;
import com.example.imersaospring.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

// Service é responsável por implementar operações de negócio
// Exemplo: operação para buscar as vendas

// Com a anotação @Service registra o SalesService como  um componente do sistema
@Service
public class SaleService {

    // Repository para poder acessar o banco de dados
    @Autowired
    private SaleRepository repository;

    @Transactional
    public SaleModel save(SaleModel saleModel) {
        return repository.save(saleModel);
    }

    @Transactional
    public void delete(SaleModel saleModel) {
        repository.delete(saleModel);
    }

    // Método para buscar todas as vendas
    // Agora usamos o repository dentro do método para que ele consulte o banco de dados e nos retorne todos os dados
    public List<SaleModel> findSales() {
        return repository.findAll();
    }

    // Paginação com data mínima e data máxima
    // Os dados trafegam como texto e aqui é convertido para data
    // Depois é necessário ir para o SaleRepository para realizar a conversão
    public Page<SaleModel> findSalesPage(String minDate, String maxDate, Pageable pageable) {

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

    // Encontrar pelo ID
    public Optional<SaleModel> findById(Long id) {
        return repository.findById(id);
    }
}

