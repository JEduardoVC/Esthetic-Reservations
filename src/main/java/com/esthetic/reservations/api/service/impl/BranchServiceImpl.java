package com.esthetic.reservations.api.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.esthetic.reservations.api.dto.BranchDTO;
import com.esthetic.reservations.api.dto.MinBranchDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Employee;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.BranchRepository;
import com.esthetic.reservations.api.repository.EmployeeRepository;
import com.esthetic.reservations.api.service.BranchService;
import com.esthetic.reservations.api.util.AppConstants;

@Service
public class BranchServiceImpl extends GenericServiceImpl<Branch, BranchDTO>
		implements BranchService {

	private UserServiceImpl userService;
	private BranchRepository branchRepository;
	private EmployeeRepository employeeRepository;

	public BranchServiceImpl(BranchRepository repository, ModelMapper modelMapper, UserServiceImpl userService,
			EmployeeRepository employeeRepository) {
		super(repository, modelMapper, Branch.class, BranchDTO.class);
		this.userService = userService;
		this.branchRepository = repository;
		this.employeeRepository = employeeRepository;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public BranchDTO save(MinBranchDTO branchDTO) {
		UserEntityDTO ownerDTO = userService.findById(branchDTO.getOwnerId());
		UserEntity owner = userService.mapToModel(ownerDTO);
		Branch newBranch = new Branch(branchDTO.getBranchName(), branchDTO.getLocation(), owner,
				branchDTO.getScheduleOpen(), branchDTO.getScheduleClose(), branchDTO.getState(),
				branchDTO.getMunicipality(), null);
		Set<Employee> employees = new HashSet<>();
		newBranch.setEmployees(employees);
		newBranch = this.branchRepository.save(newBranch);
		// Set employees
		if (branchDTO.getEmployeesIds() != null) {
			for (Long employeeId : branchDTO.getEmployeesIds()) {
				Employee employee = this.employeeRepository.findById(employeeId)
						.orElseThrow(() -> new ResourceNotFoundException("Empleado", "no encontrado", "id",
								String.valueOf(employeeId)));
				employee.getWorkingBranches().add(newBranch);
				employees.add(employee);
			}
		}
		newBranch.setEmployees(employees);
		Branch savedBranch = getRepository().save(newBranch);
		for (Employee employee : savedBranch.getEmployees()) {
			this.employeeRepository.save(employee);
		}
		return mapToDTO(savedBranch);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public BranchDTO update(MinBranchDTO editedBranchDTO, Long id) {
		Branch branch = getRepository().findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Sucursal", "no encontrada", "id", String.valueOf(id)));
		UserEntityDTO ownerDTO = userService.findById(editedBranchDTO.getOwnerId());
		UserEntity owner = userService.mapToModel(ownerDTO);
		Branch editedBranch = new Branch(editedBranchDTO.getBranchName(), editedBranchDTO.getLocation(), owner,
				editedBranchDTO.getScheduleOpen(), editedBranchDTO.getScheduleClose(), editedBranchDTO.getState(),
				editedBranchDTO.getMunicipality(), null);
		Set<Employee> formerEmployees = branch.getEmployees();
		branch.copy(editedBranch);
		Set<Employee> currentEmployees = new HashSet<>();
		currentEmployees.addAll(formerEmployees);
		if (editedBranchDTO.getEmployeesIds() != null) {
			Set<Employee> newEmployees = new HashSet<>();
			for (Long employeeId : editedBranchDTO.getEmployeesIds()) {
				Employee employee = this.employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Empleado", "no encontrado", "id", String.valueOf(employeeId)));
				newEmployees.add(employee);
			}
			if(currentEmployees.size() >= newEmployees.size()){
				currentEmployees.retainAll(newEmployees); // Intersection
			} else {
				currentEmployees.addAll(newEmployees);
			}
		}
		branch.setEmployees(currentEmployees);
		Branch savedBranch = getRepository().save(branch);
		for(Employee employee : savedBranch.getEmployees()){
			employee.getWorkingBranches().add(editedBranch);
			this.employeeRepository.save(employee);
		}
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
						entity.getState(), entity.getMunicipality(),
						entity.getEmployees().stream().map(emp -> emp.getId()).collect(Collectors.toSet())))
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

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long id) {
		Branch branch = this.branchRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Sucursal", "no encontrada", "id", String.valueOf(id)));

		// Delete relation with employees
		for (Employee employee : branch.getEmployees()) {
			employee.getWorkingBranches().remove(branch);
		}
		branch.setEmployees(new HashSet<>());

		this.branchRepository.delete(this.branchRepository.save(branch));
	}

}