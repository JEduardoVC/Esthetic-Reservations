package com.esthetic.reservations.api.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.esthetic.reservations.api.dto.ServiceDTO;
import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.repository.ServiceRepository;
import com.esthetic.reservations.api.service.ServiceService;

@org.springframework.stereotype.Service
public class SeriviceServiceImpl extends GenericServiceImpl<Service, ServiceDTO>
		implements ServiceService {

	private ServiceRepository serviceRespository;
	
	@Autowired
	public SeriviceServiceImpl(ServiceRepository repository, ModelMapper modelMapper) {
		super(repository, modelMapper, Service.class, ServiceDTO.class);
	}

	@Override
	public List<Service> findAllById_branch(int id) {
		return serviceRespository.findAllById_branch(id);
	}
}
