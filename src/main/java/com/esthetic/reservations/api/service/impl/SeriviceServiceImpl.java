package com.esthetic.reservations.api.service.impl;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.esthetic.reservations.api.dto.BranchDTO;
import com.esthetic.reservations.api.dto.MinBranchDTO;
import com.esthetic.reservations.api.dto.MinService;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.ServiceDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Service;
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
	
	public ServiceDTO findById(Long id) {
        Service entity = getRepository().findById(id).orElseThrow(() -> new ResourceNotFoundException("Servicio", "no encontrado"));
        return mapToDTO(entity);
    }
	
	public ServiceDTO save(MinService servicio) {
		BranchDTO sucursal = branchServiceImpl.findById(servicio.getId_branch());
		Branch newSucursal = branchServiceImpl.mapToModel(sucursal);
		Time duration = new Time(servicio.getDuration());
		Service service = new Service(servicio.getService_name(), duration, servicio.getPrice(), newSucursal);
		return mapToDTO(getRepository().save(service));
	}
	
	public ServiceDTO update(MinService servicio, Long id) {
		Service service = getRepository().findById(id).orElseThrow(() -> new BadRequestException("Servicio", "No existe"));
		BranchDTO sucursal = branchServiceImpl.findById(servicio.getId_branch());
		Branch newSucursal = branchServiceImpl.mapToModel(sucursal);
		Time duration = new Time(servicio.getDuration());
		Service newService = new Service(id, servicio.getService_name(), duration, servicio.getPrice(), newSucursal);
		return mapToDTO(getRepository().save(newService));
	}

	public ResponseDTO<ServiceDTO> findAllIdBranch(Long id) {
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(id));
		ArrayList<Service> services = serviceRespository.findAllByIdBranch(sucursal);
		ArrayList<ServiceDTO> newServices = new ArrayList<>();
		for (Service service : services) {
			newServices.add(mapToDTO(service));
		}
		ResponseDTO<ServiceDTO> response = new ResponseDTO<>();
		response.setContent(newServices);
		return response;
	}
}
