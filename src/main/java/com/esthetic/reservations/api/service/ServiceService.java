package com.esthetic.reservations.api.service;

import java.util.List;

import com.esthetic.reservations.api.dto.MinService;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.ServiceDTO;
import com.esthetic.reservations.api.model.Service;

public interface ServiceService {
	ResponseDTO<ServiceDTO> findAllIdBranch(Long id);
}