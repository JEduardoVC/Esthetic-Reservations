package com.esthetic.reservations.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.esthetic.reservations.api.dto.BranchCreationDTO;
import com.esthetic.reservations.api.dto.BranchDTO;
import com.esthetic.reservations.api.dto.GenericEntityValueDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.service.impl.BranchServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/branch")
public class BranchController {

    private BranchServiceImpl branchService;

    @Autowired
    public BranchController(BranchServiceImpl branchService) {
        this.branchService = branchService;
    }

    @GetMapping("/all")
    public ResponseDTO<BranchDTO> findAll(
            @RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return branchService.findAll(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/all/filter")
    public ResponseDTO<BranchDTO> findAllBy(
            @RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "filterBy", defaultValue = "owner", required = true) String filterBy,
            @Valid @RequestBody GenericEntityValueDTO genericEntityValueDTO) {
        
        switch (filterBy) {
            case "owner":
                Long ownerId = Long.parseLong(genericEntityValueDTO.getValue());
                return branchService.findAllByOwnerId(pageNumber, pageSize, sortBy, sortBy, ownerId);
            default:
                throw new BadRequestException("Filtro", "no esta implementado", "filtro", filterBy);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(branchService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BranchDTO> save(@Valid @RequestBody BranchCreationDTO branchDTO) {
        return new ResponseEntity<>(branchService.save(branchDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> update(@Valid @RequestBody BranchCreationDTO branchDTO,
            @PathVariable(name = "id") Long id) {
        BranchDTO branchResponse = branchService.update(branchDTO, id);
        return new ResponseEntity<>(branchResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
        branchService.delete(id);
        return new ResponseEntity<>("Sucursal eliminada", HttpStatus.OK);
    }

}
