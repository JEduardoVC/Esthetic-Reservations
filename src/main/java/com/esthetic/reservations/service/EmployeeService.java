package com.esthetic.reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.implemen.service.GenericImpService;
import com.esthetic.reservations.model.Employee;
import com.esthetic.reservations.repository.EmployeeRepository;

@Service
public class EmployeeService implements GenericImpService<Employee> {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public Employee update(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> findAll() {
		return (List<Employee>) employeeRepository.findAll();
	}

	@Override
	public Employee find(Integer id) {
		Optional<Employee> tarea = employeeRepository.findById(id);
		return tarea.orElse(null);
	}

	@Override
	public void delete(Integer id) {
		employeeRepository.deleteById(id);
		
	}
	
	@Override
	public List<Employee> where(String columna, String valor) {
		return (List<Employee>) employeeRepository.where(columna, valor);
	}
}
