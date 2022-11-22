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

import com.esthetic.reservations.api.dto.MinBranchDTO;
import com.esthetic.reservations.api.dto.BranchDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.BranchRepository;
import com.esthetic.reservations.api.service.BranchService;
import com.esthetic.reservations.api.util.AppConstants;

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

	public BranchDTO save(MinBranchDTO branchDTO) {
		UserEntityDTO ownerDTO = userService.findById(branchDTO.getOwnerId());
		UserEntity owner = userService.mapToModel(ownerDTO);
		Branch newBranch = new Branch(branchDTO.getBranchName(), branchDTO.getLocation(), owner,
				branchDTO.getScheduleOpen(), branchDTO.getScheduleClose(), branchDTO.getState(),
				branchDTO.getMunicipality());
		return mapToDTO(getRepository().save(newBranch));
	}

	public BranchDTO update(MinBranchDTO editedBranchDTO, Long id) {
		Branch branch = getRepository().findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Sucursal", "no encontrada", "id", String.valueOf(id)));
		UserEntityDTO ownerDTO = userService.findById(editedBranchDTO.getOwnerId());
		UserEntity owner = userService.mapToModel(ownerDTO);
		Branch editedBranch = new Branch(editedBranchDTO.getBranchName(), editedBranchDTO.getLocation(), owner,
				editedBranchDTO.getScheduleOpen(), editedBranchDTO.getScheduleClose(), editedBranchDTO.getState(),
				editedBranchDTO.getMunicipality());
		branch.copy(editedBranch);
		return mapToDTO(getRepository().save(branch));
	}

	@Override
	public ResponseDTO<MinBranchDTO> findAllByOwnerId(int pageNumber, int pageSize, String sortBy, String sortDir,
			Long ownerId) {
		// Throws error when no vaild owner id
		if (!userService.validOwnerId(ownerId)) {
			throw new BadRequestException("Usuario", "no es", "rol", AppConstants.OWNER_ROLE_NAME);
		}
		;
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Branch> branches = branchRepository.findAllByOwnerId(ownerId, pageable);
		ArrayList<Branch> entitiesList = new ArrayList<>(branches.getContent());
		// To JSON list
		ArrayList<MinBranchDTO> content = entitiesList.stream()
				.map(entity -> new MinBranchDTO(entity.getId(), entity.getBranchName(), entity.getLocation(),
						entity.getOwner().getId(), entity.getScheduleOpen(), entity.getScheduleClose(),
						entity.getState(), entity.getMunicipality()))
				.collect(Collectors.toCollection(ArrayList::new));
		ResponseDTO<MinBranchDTO> userResponseDTO = new ResponseDTO<>();
		userResponseDTO.setContent(content);
		userResponseDTO.setPageNumber(pageNumber);
		userResponseDTO.setPageSize(pageSize);
		userResponseDTO.setCount(branches.getTotalElements());
		userResponseDTO.setTotalPages(branches.getTotalPages());
		userResponseDTO.setLast(branches.isLast());
		return userResponseDTO;
	}

}