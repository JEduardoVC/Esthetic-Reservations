package com.esthetic.reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.implemen.service.GenericImpService;
import com.esthetic.reservations.model.Appointment;
import com.esthetic.reservations.repository.GenericRepository;

@Service
public class AppointmentService implements GenericImpService<Appointment> {

	@Autowired
	private GenericRepository<Appointment> appointmentRepository;

	@Override
	public Appointment save(Appointment appointment) {
		return appointmentRepository.save(appointment);
	}

	@Override
	public Appointment update(Appointment appointment) {
		return appointmentRepository.save(appointment);
	}

	@Override
	public List<Appointment> findAll() {
		return (List<Appointment>) appointmentRepository.findAll();
	}

	@Override
	public Appointment find(Integer id) {
		Optional<Appointment> tarea = appointmentRepository.findById(id);
		return tarea.orElse(null);
	}

	@Override
	public void delete(Integer id) {
		appointmentRepository.deleteById(id);
		
	}
	
	@Override
	public List<Appointment> where(String table, String columna, String valor) {
		return (List<Appointment>) appointmentRepository.where(table, columna, valor);
	}
}
