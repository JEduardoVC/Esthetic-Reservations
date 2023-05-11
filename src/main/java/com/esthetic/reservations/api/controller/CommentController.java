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

import com.esthetic.reservations.api.dto.CommentDTO;
import com.esthetic.reservations.api.dto.CommentMinDTO;
import com.esthetic.reservations.api.dto.DoubleDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.EstheticAppException;
import com.esthetic.reservations.api.service.impl.CommentServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private CommentServiceImpl commentService;

    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<CommentDTO> findBy(@RequestParam(value = "by", required = true) String filterBy,
            @RequestParam(value = "filterTo", required = true) String filterTo) {
        switch (filterBy) {
            case "appointment":
                Long appointmentId = Long.parseLong(filterTo);
                return ResponseEntity.ok(commentService.findByAppointmentId(appointmentId));
            default:
                throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Campo no válido");
        }
    }

    @GetMapping("/all")
    public ResponseDTO<CommentDTO> findAll(
            @RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "by", required = false, defaultValue = "na") String filterBy,
            @RequestParam(value = "filterTo", required = false) String filterTo) {
        switch (filterBy) {
            case "client":
                Long clientId = Long.parseLong(filterTo);
                return commentService.findAllByClientId(pageNumber, pageSize, sortBy, sortDir, clientId);
            case "employee":
                Long employeeId = Long.parseLong(filterTo);
                return commentService.findAllByEmployeeId(pageNumber, pageSize, sortBy, sortDir, employeeId);
            case "userEmployee":
                Long userId = Long.parseLong(filterTo);
                return commentService.findAllByEmployeeUserId(pageNumber, pageSize, sortBy, sortDir, userId);
            default:
                return commentService.findAll(pageNumber, pageSize, sortBy, sortDir);
        }
    }

    @GetMapping("/last/employee/{id}")
    public ResponseEntity<CommentDTO> findLastByEmployee(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(commentService.findLastByEmployeeId(id));
    }

    @GetMapping("/average/employee/{id}")
    public ResponseEntity<DoubleDTO> findEmployeeAverageRating(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(commentService.findEmployeeAverageRating(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CommentDTO> save(@Valid @RequestBody CommentMinDTO commentDTO) {
        return new ResponseEntity<>(commentService.save(commentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> update(@Valid @RequestBody CommentMinDTO commentDTO,
            @PathVariable(name = "id") Long id) {
        CommentDTO roleReponse = commentService.update(commentDTO, id);
        return new ResponseEntity<>(roleReponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
        commentService.delete(id);
        return new ResponseEntity<>("Comentario eliminado", HttpStatus.OK);
    }

}
