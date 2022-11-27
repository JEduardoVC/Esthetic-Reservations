package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.InventoryDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;

public interface InventoryService {
	ResponseDTO<InventoryDTO> findAllByBranchId(Long id);
}
