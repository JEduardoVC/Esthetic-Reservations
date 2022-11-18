package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.BranchDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.model.Branch;

public interface BranchService extends GenericService<Branch, BranchDTO> {

    public ResponseDTO<BranchDTO> findAllByOwnerId(int pageNumber, int pageSize, String sortBy, String sortDir,
            Long ownerId);
}
