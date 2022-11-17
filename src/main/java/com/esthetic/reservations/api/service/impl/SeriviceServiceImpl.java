package com.esthetic.reservations.api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.esthetic.reservations.api.dto.ServiceDTO;
import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.repository.ServiceRepository;
import com.esthetic.reservations.api.service.ServiceService;

@org.springframework.stereotype.Service
public class SeriviceServiceImpl extends GenericServiceImpl<Service, ServiceDTO>
		implements ServiceService {

	@Autowired
	public SeriviceServiceImpl(ServiceRepository repository, ModelMapper modelMapper) {
		super(repository, modelMapper, Service.class, ServiceDTO.class);
	}

}
