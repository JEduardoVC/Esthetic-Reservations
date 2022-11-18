package com.esthetic.reservations.api.service;

import java.util.List;

import com.esthetic.reservations.api.model.Service;

public interface ServiceService {
	List<Service> findAllById_branch(int id);
}
