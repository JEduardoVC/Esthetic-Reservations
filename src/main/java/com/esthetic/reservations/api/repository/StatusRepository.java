package com.esthetic.reservations.api.repository;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.model.Status;

@Repository
@Transactional
@Component("StatusRepository")
public interface StatusRepository extends GenericRepository<Status, Long> {

}