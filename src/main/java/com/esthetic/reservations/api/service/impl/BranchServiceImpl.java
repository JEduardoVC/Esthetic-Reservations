package com.esthetic.reservations.api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.BranchDTO;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.repository.BranchRepository;
import com.esthetic.reservations.api.service.BranchService;

@Service
public class BranchServiceImpl extends GenericServiceImpl<Branch, BranchDTO>
		implements BranchService {

	@Autowired
	public BranchServiceImpl(BranchRepository repository, ModelMapper modelMapper) {
		super(repository, modelMapper, Branch.class, BranchDTO.class);
		//TODO Auto-generated constructor stub
	}

}