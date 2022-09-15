package com.esthetic.reservations.implemen.service;


import java.util.List;

public interface GenericImpService<T> {
	
	List<T> findAll();
	
	T find(Integer id);
	
	void delete(Integer id);
	
	public T save(T t);
	
	public T update(T t);
}
