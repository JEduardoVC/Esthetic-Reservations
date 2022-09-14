package com.esthetic.reservations.implemen.service;

import java.util.List;

import com.esthetic.reservations.model.Client;

public interface ImplClient {
	List<Client> findAll();
	
	Client find(Integer id);
	
	void delete(Integer id);
	
	public Client save(Client t);
	
	public Client update(Client t);
	
	public List<Client> where(String columna, String valor);
}
