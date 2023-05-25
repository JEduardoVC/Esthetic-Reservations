package com.esthetic.reservations.api.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.CommentDTO;
import com.esthetic.reservations.api.dto.CommentMinDTO;
import com.esthetic.reservations.api.dto.DoubleDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Comment;
import com.esthetic.reservations.api.model.Employee;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.CommentRepository;
import com.esthetic.reservations.api.service.CommentService;

@Service
public class CommentServiceImpl extends GenericServiceImpl<Comment, CommentDTO>
        implements CommentService {

    private CommentRepository commentRepository;
    private AppointmentServiceImpl appointmentServiceImpl;
    private UserServiceImpl userServiceImpl;
    private EmployeeServiceImpl employeeServiceImpl;

    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, AppointmentServiceImpl appointmentServiceImpl, UserServiceImpl userServiceImpl, EmployeeServiceImpl employeeServiceImpl) {
        super(commentRepository, modelMapper, Comment.class, CommentDTO.class);
        this.commentRepository = commentRepository;
        this.appointmentServiceImpl = appointmentServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @Override
    public CommentDTO findByAppointmentId(Long id) {
        Comment comment = commentRepository.findByAppointmentId(id).orElseThrow(() -> new ResourceNotFoundException("Comentario", "no encontrado", "id de la reservación", String.valueOf(id)));
        return mapToDTO(comment);
    }
    
    @Override
    public CommentDTO findLastByEmployeeId(Long id) {
        Comment comment = commentRepository.findTopByEmployeeIdOrderByDateDesc(id).orElseThrow(() -> new ResourceNotFoundException("Comentario", "no encontrado", "id de empleado", String.valueOf(id)));
        return mapToDTO(comment);
    }

    @Override
    public ResponseDTO<CommentDTO> findAllByClientId(int pageNumber, int pageSize, String sortBy, String sortDir,Long id) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Comment> entities = this.commentRepository.findAllByClientId(id, pageable);
        ArrayList<Comment> entitiesList = new ArrayList<>(entities.getContent());
        // To JSON list
        ArrayList<CommentDTO> content = entitiesList.stream().map(entity -> mapToDTO(entity))
                .collect(Collectors.toCollection(ArrayList::new));
        ResponseDTO<CommentDTO> userResponseDTO = new ResponseDTO<>();
        userResponseDTO.setContent(content);
        userResponseDTO.setPageNumber(pageNumber);
        userResponseDTO.setPageSize(pageSize);
        userResponseDTO.setCount(entities.getTotalElements());
        userResponseDTO.setTotalPages(entities.getTotalPages());
        userResponseDTO.setLast(entities.isLast());
        return userResponseDTO;
    }

    @Override
    public ResponseDTO<CommentDTO> findAllByEmployeeUserId(int pageNumber, int pageSize, String sortBy, String sortDir,Long id) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Comment> entities = this.commentRepository.findAllByEmployeeUserId(id, pageable);
        ArrayList<Comment> entitiesList = new ArrayList<>(entities.getContent());
        // To JSON list
        ArrayList<CommentDTO> content = entitiesList.stream().map(entity -> mapToDTO(entity))
                .collect(Collectors.toCollection(ArrayList::new));
        ResponseDTO<CommentDTO> userResponseDTO = new ResponseDTO<>();
        userResponseDTO.setContent(content);
        userResponseDTO.setPageNumber(pageNumber);
        userResponseDTO.setPageSize(pageSize);
        userResponseDTO.setCount(entities.getTotalElements());
        userResponseDTO.setTotalPages(entities.getTotalPages());
        userResponseDTO.setLast(entities.isLast());
        return userResponseDTO;
    }

    @Override
    public ResponseDTO<CommentDTO> findAllByEmployeeId(int pageNumber, int pageSize, String sortBy, String sortDir, Long id) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Comment> entities = this.commentRepository.findAllByEmployeeId(id, pageable);
        ArrayList<Comment> entitiesList = new ArrayList<>(entities.getContent());
        // To JSON list
        ArrayList<CommentDTO> content = entitiesList.stream().map(entity -> mapToDTO(entity))
                .collect(Collectors.toCollection(ArrayList::new));
        ResponseDTO<CommentDTO> userResponseDTO = new ResponseDTO<>();
        userResponseDTO.setContent(content);
        userResponseDTO.setPageNumber(pageNumber);
        userResponseDTO.setPageSize(pageSize);
        userResponseDTO.setCount(entities.getTotalElements());
        userResponseDTO.setTotalPages(entities.getTotalPages());
        userResponseDTO.setLast(entities.isLast());
        return userResponseDTO;
    }

    @Override
    public DoubleDTO findEmployeeAverageRating(Long id) {
        Double avg = this.commentRepository.findEmployeeAverageRating(id);
        DoubleDTO doubleDTO = new DoubleDTO(avg);
        return doubleDTO;
    }

    public CommentDTO save(CommentMinDTO dto) {
        Appointment appointment = this.appointmentServiceImpl.mapToModel(this.appointmentServiceImpl.findById(dto.getAppointmentId()));
        UserEntity client = this.userServiceImpl.mapToModel(this.userServiceImpl.findById(dto.getClientId()));
        Employee employee = this.employeeServiceImpl.mapToModel(this.employeeServiceImpl.findById(dto.getEmployeeId()));
        Comment comment = new Comment(dto.getRating(), dto.getComment(), Date.valueOf(LocalDate.now(ZoneId.systemDefault())), appointment, client, employee);
        return mapToDTO(this.commentRepository.save(comment));
    }

    public CommentDTO update(CommentMinDTO dto, Long id) {
        Comment oldComment = this.commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comentario", "no encontrado", "id", String.valueOf(id)));
        Appointment appointment = this.appointmentServiceImpl.mapToModel(this.appointmentServiceImpl.findById(dto.getAppointmentId()));
        UserEntity client = this.userServiceImpl.mapToModel(this.userServiceImpl.findById(dto.getClientId()));
        Employee employee = this.employeeServiceImpl.mapToModel(this.employeeServiceImpl.findById(dto.getEmployeeId()));
        Comment newComment = new Comment(dto.getRating(), dto.getComment(), Date.valueOf(LocalDate.now(ZoneId.systemDefault())), appointment, client, employee);
        oldComment.copy(newComment);
        return mapToDTO(this.commentRepository.save(oldComment));
    }

}
