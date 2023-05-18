package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.CommentDTO;
import com.esthetic.reservations.api.dto.DoubleDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.model.Comment;

public interface CommentService extends GenericService<Comment, CommentDTO> {

    public CommentDTO findByAppointmentId(Long id);

    public CommentDTO findLastByEmployeeId(Long id);

    public ResponseDTO<CommentDTO> findAllByClientId(int pageNumber, int pageSize, String sortBy, String sortDir,
            Long id);

    public ResponseDTO<CommentDTO> findAllByEmployeeUserId(int pageNumber, int pageSize, String sortBy, String sortDir,
            Long id);

    public ResponseDTO<CommentDTO> findAllByEmployeeId(int pageNumber, int pageSize, String sortBy, String sortDir,
            Long id);

    public DoubleDTO findEmployeeAverageRating(Long id);

}
