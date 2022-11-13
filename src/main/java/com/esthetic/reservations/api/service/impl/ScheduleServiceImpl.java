package com.esthetic.reservations.api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.ScheduleDTO;
import com.esthetic.reservations.api.model.Schedule;
import com.esthetic.reservations.api.repository.ScheduleRepository;
import com.esthetic.reservations.api.service.ScheduleService;

@Service
public class ScheduleServiceImpl extends GenericServiceImpl<Schedule, ScheduleDTO>
		implements ScheduleService {

	@Autowired
	public ScheduleServiceImpl(ScheduleRepository repository, ModelMapper modelMapper) {
		super(repository, modelMapper, Schedule.class, ScheduleDTO.class);
	}

}
