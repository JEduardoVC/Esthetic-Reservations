package com.esthetic.reservations.api.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;

@Repository
@Transactional
@Component("AppointmentRepository")
public interface AppointmentRepository extends GenericRepository<Appointment, Long> {
	
	ArrayList<Appointment> findAllByIdBranch(Branch idbranch);
}