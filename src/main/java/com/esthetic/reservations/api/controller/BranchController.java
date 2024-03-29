package com.esthetic.reservations.api.controller;

import javax.annotation.security.RolesAllowed;
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

import com.esthetic.reservations.api.dto.BranchDTO;
import com.esthetic.reservations.api.dto.MessageDTO;
import com.esthetic.reservations.api.dto.MinBranchDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.service.impl.BranchServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/branch")
public class BranchController {

    private BranchServiceImpl branchService;

    public BranchController(BranchServiceImpl branchService) {
        this.branchService = branchService;
    }

    @GetMapping("/all")
    @RolesAllowed({AppConstants.ADMIN_ROLE_NAME, AppConstants.CLIENT_ROLE_NAME})
    public ResponseDTO<BranchDTO> findAll(
            @RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return branchService.findAll(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/all/filter")
    public ResponseDTO<MinBranchDTO> findAllBy(
            @RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "filterBy", defaultValue = "owner", required = true) String filterBy,
            @RequestParam(value = "filterTo", required = true) String filterTo) {

        switch (filterBy) {
            case "owner":
                Long ownerId = Long.parseLong(filterTo);
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
    public ResponseEntity<BranchDTO> save(@Valid @RequestBody MinBranchDTO branchDTO) {
        return new ResponseEntity<>(branchService.save(branchDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> update(@Valid @RequestBody MinBranchDTO branchDTO,
            @PathVariable(name = "id") Long id) {
        BranchDTO branchResponse = branchService.update(branchDTO, id);
        return new ResponseEntity<>(branchResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable(name = "id") Long id) {
        branchService.delete(id);
        return new ResponseEntity<>(new MessageDTO("Sucursal eliminada"), HttpStatus.OK);
    }

}
