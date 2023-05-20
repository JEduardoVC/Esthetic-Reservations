package com.esthetic.reservations.api.service.impl;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.management.StringValueExp;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.esthetic.reservations.api.dto.AppointmentDTO;
import com.esthetic.reservations.api.dto.GenericModelDTO;
import com.esthetic.reservations.api.dto.MinAppointmentDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.AppointmentRepository;
import com.esthetic.reservations.api.service.AppointmentService;
import com.esthetic.reservations.api.service.MailService;
import com.esthetic.reservations.api.util.Util;

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
	MailService mailService;
	
	@Autowired
	Util util;

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
	
	public ResponseDTO<AppointmentDTO> findAllByIdClient(Long id) {
		UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(id));
		List<Appointment> listaCitas = appointmentRepository.findAllByIdClient(usuario);
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
	
	public AppointmentDTO save(MinAppointmentDTO cita) {
		UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(cita.getId_client()));
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(cita.getId_branch()));
		UserEntity empleado = userServiceImpl.mapToModel(userServiceImpl.findById(cita.getId_employee()));
		Date date_created = Date.valueOf(LocalDate.now());
		Date appointment_date = Date.valueOf(cita.getappointment_date());
		new Appointment();
		Appointment appointment = new Appointment(date_created, appointment_date, cita.getAppointment_time(), usuario, empleado, null, sucursal);
		appointment.setServicios(changeModel(cita.getCantidad(), cita.getId_service()));
		return mapToDTO(appointmentRepository.save(appointment));
	}
	
	public AppointmentDTO update(MinAppointmentDTO cita, Long id) {
		Appointment citaActual = appointmentRepository.findById(id).get();
		UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(cita.getId_client()));
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(cita.getId_branch()));
		Date date_created = Date.valueOf(LocalDate.now());
		citaActual.setDate_created(date_created);
		citaActual.setAppointmnet_time(cita.getAppointment_time());
		citaActual.setAppointment_Date(Date.valueOf(cita.getappointment_date()));
		citaActual.setId_branch(sucursal);
		citaActual.setId_client(usuario);
		citaActual.setServicios(changeModel(cita.getCantidad(), cita.getId_service()));
		return mapToDTO(appointmentRepository.save(citaActual));
	}
	
	public String eliminar(Long id) {
		Appointment cita = appointmentRepository.findById(id).get();
		cita.setServicios(new ArrayList<>());
		appointmentRepository.save(cita);
		appointmentRepository.delete(cita);
		return "Cita Eliminada Correctamente";
	}
	
	private List<Service> changeModel(ArrayList<Long> cantidad, ArrayList<Long> lista) {
		List<Service> listaServicio = new ArrayList<>();
		for(int i=0; i<cantidad.size(); i++) {
			for(int j=0; j<cantidad.get(i); j++) {
				listaServicio.add(serviceImpl.mapToModel(serviceImpl.findById(lista.get(i))));
			}
		}
		return listaServicio;
	}
	
	public ResponseDTO<AppointmentDTO> findAllByDate(Long id, String date) {
		List<Appointment> listaCitas = appointmentRepository.findByDate(date, String.valueOf(id));
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
	
	public ResponseDTO<AppointmentDTO> findByIdAndbyBranch(Long id, Long id_branch) {
		List<Appointment> listaCitas = appointmentRepository.findByIdAndByBranch(String.valueOf(id), String.valueOf(id_branch));
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
	
	public ResponseDTO<AppointmentDTO> findByDateAndEmployee(Long id_branch, String date, Long id_employee) {
		List<Appointment> listaCitas = appointmentRepository.findByIdEmployeeAndByBranch(date, String.valueOf(id_branch), String.valueOf(id_employee));
		ResponseDTO<AppointmentDTO> response = new ResponseDTO<>();
		response.setContent(changeModelToDTO(listaCitas));
		return response;
	}
	
	private ArrayList<AppointmentDTO> changeModelToDTO(List<Appointment> lista) {
		ArrayList<AppointmentDTO> arregloCitas = new ArrayList<>();
		for(Appointment appointment : lista) {
			AppointmentDTO citaDTO = mapToDTO(appointment);
			citaDTO.setId_service(appointment.getServicios());
			arregloCitas.add(citaDTO);
		}
		return arregloCitas;
	}
}