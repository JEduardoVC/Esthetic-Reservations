package com.esthetic.reservations.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.NewSaleDTO;
import com.esthetic.reservations.api.dto.SaleDTO;
import com.esthetic.reservations.api.service.impl.SaleServiceImpl;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    private SaleServiceImpl saleService;

    public SaleController(SaleServiceImpl saleService) {
        this.saleService = saleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SaleDTO> save(@Valid @RequestBody NewSaleDTO saleDTO) {
        return new ResponseEntity<>(saleService.save(saleDTO), HttpStatus.CREATED);
    }

}
