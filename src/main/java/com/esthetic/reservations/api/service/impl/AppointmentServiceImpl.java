package com.esthetic.reservations.api.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.esthetic.reservations.api.dto.AppointmentDTO;
import com.esthetic.reservations.api.dto.MinAppointmentDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.AppointmentRepository;
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

	public AppointmentServiceImpl(AppointmentRepository repository, ModelMapper modelMapper) {
		super(repository, modelMapper, Appointment.class, AppointmentDTO.class);
	}
	
	public AppointmentDTO getId(Long id) {
		Appointment cita = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id", "No existe"));
		AppointmentDTO citaDTO = mapToDTO(cita);
		citaDTO.setId_service(cita.getServicios());
		return citaDTO;
	}
	
	public ResponseDTO<AppointmentDTO> findAllByIdBranch(Long id) {
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(id));
		List<Appointment> listaCitas = appointmentRepository.findAllByIdBranch(sucursal);
		ArrayList<AppointmentDTO> arregloCitas = new ArrayList<>();
		for(Appointment appointment : listaCitas) {
			AppointmentDTO citaDTO = mapToDTO(appointment);
			citaDTO.setId_service(appointment.getServicios());
			arregloCitas.add(citaDTO);
		}
		ResponseDTO<AppointmentDTO> response = new ResponseDTO<>();
		response.setContent(arregloCitas);
		return response;
	}
	
	public String save(MinAppointmentDTO cita) {
		UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(cita.getId_client()));
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(cita.getId_branch()));
		Date date_created = Date.valueOf(LocalDate.now());
		Date appointment_date = Date.valueOf(cita.getappointment_date());
		Appointment appointment = new Appointment(date_created, appointment_date, cita.getAppointment_time(), usuario, null, null, sucursal);
		appointment.setServicios(changeModel(cita.getId_service()));
		appointmentRepository.save(appointment);
		return "Cita Registrada Exitosamente";
	}
	
	public String update(MinAppointmentDTO cita, Long id) {
		Appointment citaActual = appointmentRepository.findById(id).get();
		UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(cita.getId_client()));
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(cita.getId_branch()));
		Date date_created = Date.valueOf(LocalDate.now());
		citaActual.setDate_created(date_created);
		citaActual.setAppointmnet_time(cita.getAppointment_time());
		citaActual.setAppointment_Date(Date.valueOf(cita.getappointment_date()));
		citaActual.setId_branch(sucursal);
		citaActual.setId_client(usuario);
		citaActual.setServicios(changeModel(cita.getId_service()));
		appointmentRepository.save(citaActual);
		return "Cita Actualizada Exitosamente";
	}
	
	public String eliminar(Long id) {
		Appointment cita = appointmentRepository.findById(id).get();
		cita.setServicios(new ArrayList<>());
		appointmentRepository.save(cita);
		return "Cita Eliminada Correctamente";
	}
	
	private List<Service> changeModel(ArrayList<Long> lista) {
		List<Service> listaServicio = new ArrayList<>();
		for(Long id_service : lista) {
			listaServicio.add(serviceImpl.mapToModel(serviceImpl.findById(id_service)));
		}
		return listaServicio;
	}
}