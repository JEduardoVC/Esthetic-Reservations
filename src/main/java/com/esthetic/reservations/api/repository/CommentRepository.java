package com.esthetic.reservations.api.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.esthetic.reservations.api.model.Comment;

@Transactional
@Component("CommentRepository")
public interface CommentRepository extends GenericRepository<Comment, Long> {

    public Optional<Comment> findByAppointmentId(Long id);

    public Page<Comment> findAllByClientId(Long id, Pageable pageable);

    public Page<Comment> findAllByEmployeeUserId(Long id, Pageable pageable);

    public Page<Comment> findAllByEmployeeId(Long id, Pageable pageable);

    // Get last comment
    public Optional<Comment> findTopByEmployeeIdOrderByDateDesc(Long id);

    @Query(value = "SELECT sum(c.rating)/count(c.id) as promedio FROM comment c WHERE c.employee_id=:id", nativeQuery = true)
	Double findEmployeeAverageRating(@Param("id") Long id);

}
