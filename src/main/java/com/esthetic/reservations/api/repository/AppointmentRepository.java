package com.esthetic.reservations.api.repository;

import java.util.ArrayList;
import java.sql.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.UserEntity;

@Transactional
@Component("AppointmentRepository")
public interface AppointmentRepository extends GenericRepository<Appointment, Long> {
	
	@Query(value = "select * from appointment where appointment_date = :date and id_branch_id = :id", nativeQuery = true)
	ArrayList<Appointment> findByDate(@Param("date") String date,@Param("id") String id);
	
	@Query(value = "select * from appointment where id_branch_id = :id_branch and id_client_id = :id", nativeQuery = true)
	ArrayList<Appointment> findByIdAndByBranch(@Param("id") String id,@Param("id_branch") String id_branch);
	
	@Query(value = "select * from appointment where appointment_date = :date and id_branch_id = :id and id_employee_id = :idEmployee", nativeQuery = true)
	ArrayList<Appointment> findByIdEmployeeAndByBranch(@Param("date") String date,@Param("id") String id, @Param("idEmployee") String idEmployee);
		
	ArrayList<Appointment> findAllByIdBranch(Branch idbranch);
	
	ArrayList<Appointment> findAllByIdClient(UserEntity idClient);
}