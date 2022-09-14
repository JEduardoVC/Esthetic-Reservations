package com.esthetic.reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.implemen.service.GenericImpService;
import com.esthetic.reservations.model.Branch;

import com.esthetic.reservations.repository.BranchRepository;

@Service
public class BranchService implements GenericImpService<Branch> {

	@Autowired
	private BranchRepository BranchRepository;

	@Override
	public Branch save(Branch branch) {
		return BranchRepository.save(branch);
	}

	@Override
	public Branch update(Branch branch) {
		return BranchRepository.save(branch);
	}

	@Override
	public List<Branch> findAll() {
		return (List<Branch>) BranchRepository.findAll();
	}

	@Override
	public Branch find(Integer id) {
		Optional<Branch> tarea = BranchRepository.findById(id);
		return tarea.orElse(null);
	}

	@Override
	public void delete(Integer id) {
		BranchRepository.deleteById(id);
		
	}
	
	@Override
	public List<Branch> where(String columna, String valor) {
		return (List<Branch>) BranchRepository.where(columna, valor);
	}
}
