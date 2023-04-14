package com.esthetic.reservations.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.MessageDTO;
import com.esthetic.reservations.api.dto.NewSaleDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.SaleDTO;
import com.esthetic.reservations.api.service.impl.SaleServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    private SaleServiceImpl saleService;

    public SaleController(SaleServiceImpl saleService) {
        this.saleService = saleService;
    }

    @GetMapping("/all")
    public ResponseDTO<SaleDTO> findAll(
            @RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return saleService.findAll(pageNumber, pageSize, sortBy, sortDir);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SaleDTO> save(@Valid @RequestBody NewSaleDTO saleDTO) {
        return new ResponseEntity<>(saleService.save(saleDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> update(@Valid @RequestBody NewSaleDTO saleDTO, @PathVariable(name = "id") Long id) {
        SaleDTO saleResponse = saleService.update(saleDTO, id);
        return new ResponseEntity<>(saleResponse, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable(name = "id") Long id){
        saleService.delete(id);
        return new ResponseEntity<>(new MessageDTO("Venta eliminada."), HttpStatus.OK);
    }

}
