package com.esthetic.reservations.api.repository;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.esthetic.reservations.api.model.Schedule;

@Transactional
@Component("ScheduleRepository")
public interface ScheduleRepository extends GenericRepository<Schedule, Long> {

}
