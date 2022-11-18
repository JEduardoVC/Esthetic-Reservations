package com.esthetic.reservations.api.service.impl;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.BranchCreationDTO;
import com.esthetic.reservations.api.dto.BranchDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.BranchRepository;
import com.esthetic.reservations.api.service.BranchService;

@Service
public class BranchServiceImpl extends GenericServiceImpl<Branch, BranchDTO>
		implements BranchService {

	private UserServiceImpl userService;
	private BranchRepository branchRepository;

	@Autowired
	public BranchServiceImpl(BranchRepository repository, ModelMapper modelMapper, UserServiceImpl userService) {
		super(repository, modelMapper, Branch.class, BranchDTO.class);
		this.userService = userService;
		this.branchRepository = repository;
	}

	public BranchDTO save(BranchCreationDTO branchDTO) {
		UserEntityDTO ownerDTO = userService.findById(branchDTO.getOwnerId());
		UserEntity owner = userService.mapToModel(ownerDTO);
		Branch newBranch = new Branch(branchDTO.getName(), branchDTO.getLocation(), owner);
		return mapToDTO(getRepository().save(newBranch));
	}

	public BranchDTO update(BranchCreationDTO editedBranchDTO, Long id) {
		Branch branch = getRepository().findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Sucursal", "no encontrada", "id", String.valueOf(id)));
		UserEntityDTO ownerDTO = userService.findById(editedBranchDTO.getOwnerId());
		UserEntity owner = userService.mapToModel(ownerDTO);
		Branch editedBranch = new Branch(editedBranchDTO.getName(), editedBranchDTO.getLocation(), owner);
		branch.copy(editedBranch);
		return mapToDTO(getRepository().save(branch));
	}

	@Override
	public ResponseDTO<BranchDTO> findAllByOwnerId(int pageNumber, int pageSize, String sortBy, String sortDir,
			Long ownerId) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Branch> branches = branchRepository.findAllByOwnerId(ownerId, pageable);
		ArrayList<Branch> entitiesList = new ArrayList<>(branches.getContent());
		// To JSON list
		ArrayList<BranchDTO> content = entitiesList.stream().map(entity -> mapToDTO(entity))
				.collect(Collectors.toCollection(ArrayList::new));
		ResponseDTO<BranchDTO> userResponseDTO = new ResponseDTO<>();
		userResponseDTO.setContent(content);
		userResponseDTO.setPageNumber(pageNumber);
		userResponseDTO.setPageSize(pageSize);
		userResponseDTO.setCount(branches.getTotalElements());
		userResponseDTO.setTotalPages(branches.getTotalPages());
		userResponseDTO.setLast(branches.isLast());
		return userResponseDTO;
	}

}