package com.example.imersaospring.controllers;

import com.example.imersaospring.dto.SalesDto;
import com.example.imersaospring.entities.SaleModel;
import com.example.imersaospring.services.SaleService;
import com.example.imersaospring.services.SmsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

// CONTROLLER vai implementar nossa API. Ele que vai disponibilizar os endpoints que o front-end vai precisar
// para acessar o backend //

// @RestController: basicamente ele é o responsável por controlar as requisições indicando quem
// deve receber as requisições para quem deve responde-las.
@RestController

//`@RequestMapping` permite definir uma rota.
@RequestMapping(value = "/sales")
public class SaleController {

    @Autowired
    private SaleService service;

    @Autowired
    private SmsService smsService;

    // Verbo HTTP para obter dados
    // Irá mostrar todas as vendas quando colocarmos o caminho localhost:8080/sales
    @GetMapping
    public List<SaleModel> findSales() {
        return service.findSales();
    }

    // Paginação com data mínima e data máxima
    // Os dados trafegam como texto e depois que vai ser convertido para data
    @GetMapping
    @RequestMapping(value = "/pageable")
    public Page<SaleModel> findSalesPage(
            @RequestParam(value = "minDate", defaultValue = "") String minDate,
            @RequestParam(value = "maxDate", defaultValue = "") String maxDate,
            Pageable pageable) {
        return service.findSalesPage(minDate, maxDate, pageable);
    }

    // Enviar notificação e passando o id como parametro
    @GetMapping("/{id}/notification")
    public void notifySms(@PathVariable Long id) {
        smsService.sendSms(id);
    }

    // Buscar venda por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneSale(@PathVariable(value = "id") Long id) {
        Optional<SaleModel> saleModelOptional = service.findById(id);
        if (saleModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(saleModelOptional.get());
    }

    // Inserir uma nova venda
    @PostMapping
    public ResponseEntity<Object> saveSales(@RequestBody SalesDto salesDto) {
        var saleModel = new SaleModel();
        BeanUtils.copyProperties(salesDto, saleModel);
        saleModel.setDate(LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(saleModel));
    }

    // Alterar uma venda
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSales(@PathVariable(value = "id") Long id, @RequestBody @Valid SalesDto salesDto) {
        Optional<SaleModel> saleModelOptional = service.findById(id);
        if (saleModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID não encontrado.");
        }
        var saleModel = new SaleModel();
        BeanUtils.copyProperties(salesDto, saleModel);
        saleModel.setId(saleModelOptional.get().getId());
        saleModel.setDate(saleModelOptional.get().getDate());
        return ResponseEntity.status(HttpStatus.OK).body(service.save(saleModel));
    }

    // Deletar uma venda
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSale(@PathVariable(value = "id") Long id) {
        Optional<SaleModel> saleModelOptional = service.findById(id);
        if (saleModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada.");
        }
        service.delete(saleModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Venda deletada com sucesso.");
    }
}
