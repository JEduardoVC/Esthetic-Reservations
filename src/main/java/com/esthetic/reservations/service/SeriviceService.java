package com.esthetic.reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.esthetic.reservations.implemen.service.GenericImpService;
import com.esthetic.reservations.model.Service;
import com.esthetic.reservations.repository.ServiceRepository;

@org.springframework.stereotype.Service
public class SeriviceService implements GenericImpService<Service> {

	@Autowired
	private ServiceRepository serviceRepository;

	@Override
	public Service save(Service service) {
		return serviceRepository.save(service);
	}

	@Override
	public Service update(Service service) {
		return serviceRepository.save(service);
	}

	@Override
	public List<Service> findAll() {
		return (List<Service>) serviceRepository.findAll();
	}

	@Override
	public Service find(Integer id) {
		Optional<Service> tarea = serviceRepository.findById(id);
		return tarea.orElse(null);
	}

	@Override
	public void delete(Integer id) {
		serviceRepository.deleteById(id);
		
	}
}
