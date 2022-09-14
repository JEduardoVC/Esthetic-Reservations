package com.esthetic.reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.implemen.service.GenericImpService;
import com.esthetic.reservations.model.Owner;
import com.esthetic.reservations.repository.OwnerRepository;

@Service
public class OwnerService implements GenericImpService<Owner> {
	@Autowired
	private OwnerRepository ownerRepository;

	@Override
	public Owner save(Owner owner) {
		return ownerRepository.save(owner);
	}

	@Override
	public Owner update(Owner owner) {
		return ownerRepository.save(owner);
	}

	@Override
	public List<Owner> findAll() {
		return (List<Owner>) ownerRepository.findAll();
	}

	@Override
	public Owner find(Integer id) {
		Optional<Owner> tarea = ownerRepository.findById(id);
		return tarea.orElse(null);
	}

	@Override
	public void delete(Integer id) {
		ownerRepository.deleteById(id);
		
	}
	
	@Override
	public List<Owner> where(String columna, String valor) {
		return (List<Owner>) ownerRepository.where(columna, valor);
	}
}
