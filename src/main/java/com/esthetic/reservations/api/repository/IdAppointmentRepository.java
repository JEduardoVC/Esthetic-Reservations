package com.esthetic.reservations.api.repository;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.model.IdAppointment;

@Repository
@Transactional
@Component("IdAppointmentRepository")
public interface IdAppointmentRepository extends GenericRepository<IdAppointment, Long> {

}
