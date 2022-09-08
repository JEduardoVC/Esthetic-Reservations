package com.esthetic.reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.esthetic.reservations.implemen.service.GenericImpService;
import com.esthetic.reservations.model.Schedule;
import com.esthetic.reservations.repository.GenericRepository;

public class ScheduleService implements GenericImpService<Schedule> {
	@Autowired
	private GenericRepository<Schedule> scheduleRepository;

	@Override
	public Schedule save(Schedule schedule) {
		return scheduleRepository.save(schedule);
	}

	@Override
	public Schedule update(Schedule schedule) {
		return scheduleRepository.save(schedule);
	}

	@Override
	public List<Schedule> findAll() {
		return (List<Schedule>) scheduleRepository.findAll();
	}

	@Override
	public Schedule find(Integer id) {
		Optional<Schedule> tarea = scheduleRepository.findById(id);
		return tarea.orElse(null);
	}

	@Override
	public void delete(Integer id) {
		scheduleRepository.deleteById(id);
		
	}
	
	@Override
	public List<Schedule> where(String table, String columna, String valor) {
		return (List<Schedule>) scheduleRepository.where(table, columna, valor);
	}
}
