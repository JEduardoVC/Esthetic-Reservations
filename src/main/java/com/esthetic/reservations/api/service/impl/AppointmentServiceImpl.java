package com.esthetic.reservations.api.service.impl;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.esthetic.reservations.api.dto.AppointmentDTO;
import com.esthetic.reservations.api.dto.MinAppointmentDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.IdAppointment;
import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.AppointmentRepository;
import com.esthetic.reservations.api.repository.IdAppointmentRepository;
import com.esthetic.reservations.api.service.AppointmentService;

@org.springframework.stereotype.Service
public class AppointmentServiceImpl extends GenericServiceImpl<Appointment, AppointmentDTO>
		implements AppointmentService {
	
	@Autowired
	AppointmentRepository appointmentRepository;
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	SeriviceServiceImpl serviceImpl;
	
	@Autowired
	BranchServiceImpl branchServiceImpl;
	
	@Autowired
	IdAppointmentRepository idAppointmentRepository;

	public AppointmentServiceImpl(AppointmentRepository repository, ModelMapper modelMapper) {
		super(repository, modelMapper, Appointment.class, AppointmentDTO.class);
	}
	
	public ResponseDTO<AppointmentDTO> findAllByIdBranch(Long id) {
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(id));
		List<Appointment> lista = appointmentRepository.findAllByIdBranch(sucursal);
		ArrayList<AppointmentDTO> listaDTO = new ArrayList<>();
		ResponseDTO<AppointmentDTO> response = new ResponseDTO<>();
		for(Appointment appointment : lista) {
			listaDTO.add(mapToDTO(appointment));
		}
		response.setContent(listaDTO);
		return response;
	}
	
	public String save(MinAppointmentDTO cita) {
		UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(cita.getId_client()));
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(cita.getId_branch()));
		Date date_created = Date.valueOf(LocalDate.now());
		Date appointment_date = Date.valueOf(cita.getappointment_date());
		List<IdAppointment> lista = new ArrayList<>();
		lista.add(idAppointmentRepository.save(new IdAppointment()));
		for(Long id_service : cita.getId_service()) {
			Service servicio = serviceImpl.mapToModel(serviceImpl.findById(id_service));
			Appointment appointment = new Appointment(date_created, appointment_date, cita.getAppointment_time(), usuario, null, servicio, null, sucursal);
			appointment.setAppointmentServices(lista);
			appointmentRepository.save(appointment);
		}
		return "Cita Registrada Exitosamente";
	}
	
	public String update(MinAppointmentDTO cita, Long id) {
		Appointment citaActual = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id", "no existe"));
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(cita.getId_branch()));
		UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(cita.getId_client()));
		Date date_created = Date.valueOf(LocalDate.now());
		Date appointment_date = Date.valueOf(cita.getappointment_date());
		List<IdAppointment> lista = new ArrayList<>();
		lista.add(idAppointmentRepository.getById((long) 0));
		for(Long id_service : cita.getId_service()) {
			Service servicio = serviceImpl.mapToModel(serviceImpl.findById(id_service));
			Appointment appointment = new Appointment(citaActual.getId(), date_created, appointment_date, cita.getAppointment_time(), usuario, null, servicio, null, sucursal);
			appointment.setAppointmentServices(lista);
			appointmentRepository.save(appointment);
		}
		return "Cita Actualizada Correctamente";
	}
}