package com.example.imersaospring.entities;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "tb_sales")
public class SaleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String sellerName;
    @Column(nullable = false)
    private Integer visited;
    @Column(nullable = false)
    private Integer deals;
    @Column(nullable = false)
    private Double amount;
    private LocalDate date;

}
