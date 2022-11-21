package com.esthetic.reservations.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.esthetic.reservations.api.dto.BranchDTO;
import com.esthetic.reservations.api.dto.MinService;
import com.esthetic.reservations.api.dto.ServiceDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.repository.BranchRepository;
import com.esthetic.reservations.api.repository.ServiceRepository;
import com.esthetic.reservations.api.service.ServiceService;

@org.springframework.stereotype.Service
public class SeriviceServiceImpl extends GenericServiceImpl<Service, ServiceDTO>
		implements ServiceService {

	private ServiceRepository serviceRespository;
	private BranchRepository branchRepository;
	
	@Autowired
	public SeriviceServiceImpl(ServiceRepository repository, ModelMapper modelMapper) {
		super(repository, modelMapper, Service.class, ServiceDTO.class);
	}
	
	public ServiceDTO save(MinService servicio) {
		Branch sucursal = branchRepository.findById(servicio.getId_branch()).orElseThrow(() -> new BadRequestException("Sucrusal", "No Existe", "", ""));
		Service service = new Service(servicio.getService_name(), servicio.getDuration(), servicio.getPrice(), sucursal);
		return mapToDTO(getRepository().save(service));
	}
	
	public ServiceDTO update(MinService servicio, Long id) {
		Branch sucursal = branchRepository.findById(servicio.getId_branch()).orElseThrow(() -> new BadRequestException("Sucrusal", "No Existe", "", ""));
		Service service = new Service(id, servicio.getService_name(), servicio.getDuration(), servicio.getPrice(), sucursal);
		return mapToDTO(getRepository().save(service));
	}

	@Override
	public List<Service> findAllByBranchId(int id) {
		return serviceRespository.findAllByBranchId(id);
	}
}
