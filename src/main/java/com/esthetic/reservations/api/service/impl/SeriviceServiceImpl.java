package com.esthetic.reservations.api.service.impl;

import java.util.List;
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

	private BranchServiceImpl branchServiceImpl;
	private ServiceRepository serviceRespository;
	
	@Autowired
	public SeriviceServiceImpl(ServiceRepository repository, ModelMapper modelMapper, BranchServiceImpl branchServiceImpl) {
		super(repository, modelMapper, Service.class, ServiceDTO.class);
		this.branchServiceImpl = branchServiceImpl;
		this.serviceRespository = repository;
	}
	
	public ServiceDTO save(MinService servicio) {
		BranchDTO sucursal = branchServiceImpl.findById(servicio.getId_branch());
		Branch newSucursal = branchServiceImpl.mapToModel(sucursal);
		Service service = new Service(servicio.getService_name(), servicio.getDuration(), servicio.getPrice(), newSucursal);
		return mapToDTO(getRepository().save(service));
	}
	
	public ServiceDTO update(MinService servicio, Long id) {
		Service service = getRepository().findById(id).orElseThrow(() -> new BadRequestException("Servicio", "No existe", null, null));
		BranchDTO sucursal = branchServiceImpl.findById(servicio.getId_branch());
		Branch newSucursal = branchServiceImpl.mapToModel(sucursal);
		Service newService = new Service(id, servicio.getService_name(), servicio.getDuration(), servicio.getPrice(), newSucursal);
		return mapToDTO(getRepository().save(newService));
	}

	@Override
	public List<Service> findAllByBranchId(int id) {
		return serviceRespository.findAllByBranchId(id);
	}
}
