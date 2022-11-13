package com.esthetic.reservations.api.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.esthetic.reservations.api.model.BaseModel;

@NoRepositoryBean
public interface GenericRepository<T extends BaseModel<T>, E extends Serializable> extends JpaRepository<T, E> {

}