package com.esthetic.reservations.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
	@Query(value = "select * from employee where :columna = :valor", nativeQuery = true)
	Iterable<Employee> where(String columna, String valor);
}

