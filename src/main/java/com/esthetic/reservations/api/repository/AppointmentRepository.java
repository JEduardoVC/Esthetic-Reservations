package com.esthetic.reservations.api.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;

@Transactional
@Component("AppointmentRepository")
public interface AppointmentRepository extends GenericRepository<Appointment, Long> {
	
	ArrayList<Appointment> findAllByIdBranch(Branch idbranch);
}