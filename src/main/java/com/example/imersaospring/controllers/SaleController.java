package com.example.imersaospring.controllers;

import com.example.imersaospring.entities.Sale;
import com.example.imersaospring.services.SaleService;
import com.example.imersaospring.services.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Sale> findSales() {
        return service.findSales();
    }

    // Paginação com data mínima e data máxima
    // Os dados trafegam como texto e depois que vai ser convertido para data
    @GetMapping
    @RequestMapping(value = "/pageable")
    public Page<Sale> findSalesPage(
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
}
