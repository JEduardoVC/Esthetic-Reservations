package com.esthetic.reservations.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.EmployeeDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.EstheticAppException;
import com.esthetic.reservations.api.service.impl.EmployeeServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private EmployeeServiceImpl employeeService;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<EmployeeDTO> findBy(@RequestParam(value = "by", required = true) String filterBy,
            @RequestParam(value = "filterTo", required = true) String filterTo) {
        switch (filterBy) {
            case "userid":
                Long userId = Long.parseLong(filterTo);
                return ResponseEntity.ok(employeeService.findByUserId(userId));
            case "username":
                return ResponseEntity.ok(employeeService.findByUserUsername(filterTo));
            case "email":
                return ResponseEntity.ok(employeeService.findByUserEmail(filterTo));
            default:
                throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Campo no válido");
        }
    }

    @GetMapping("/all")
    public ResponseDTO<EmployeeDTO> findAll(
            @RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "by", required = false, defaultValue = "na") String filterBy,
            @RequestParam(value = "filterTo", required = false) String filterTo) {
        return employeeService.findAll(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

}
