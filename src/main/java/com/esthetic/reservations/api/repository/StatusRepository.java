package com.esthetic.reservations.api.repository;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.esthetic.reservations.api.model.Status;

@Transactional
@Component("StatusRepository")
public interface StatusRepository extends GenericRepository<Status, Long> {

}