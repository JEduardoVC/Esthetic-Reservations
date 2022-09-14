package com.esthetic.reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.esthetic.reservations.implemen.service.GenericImpService;
import com.esthetic.reservations.model.Status;
import com.esthetic.reservations.repository.StatusRepository;

public class StatusService implements GenericImpService<Status> {
	@Autowired
	private StatusRepository statusRepository;

	@Override
	public Status save(Status status) {
		return statusRepository.save(status);
	}

	@Override
	public Status update(Status status) {
		return statusRepository.save(status);
	}

	@Override
	public List<Status> findAll() {
		return (List<Status>) statusRepository.findAll();
	}

	@Override
	public Status find(Integer id) {
		Optional<Status> tarea = statusRepository.findById(id);
		return tarea.orElse(null);
	}

	@Override
	public void delete(Integer id) {
		statusRepository.deleteById(id);
		
	}
	
	@Override
	public List<Status> where(String columna, String valor) {
		return (List<Status>) statusRepository.where(columna, valor);
	}
}
