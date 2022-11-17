package com.esthetic.reservations.api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.AppointmentDTO;
import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.repository.AppointmentRepository;
import com.esthetic.reservations.api.service.AppointmentService;

@Service
public class AppointmentServiceImpl extends GenericServiceImpl<Appointment, AppointmentDTO>
		implements AppointmentService {

	@Autowired
	public AppointmentServiceImpl(AppointmentRepository repository, ModelMapper modelMapper) {
		super(repository, modelMapper, Appointment.class, AppointmentDTO.class);
		// TODO Auto-generated constructor stub
	}

}