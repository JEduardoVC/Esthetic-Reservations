package com.esthetic.reservations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.implemen.service.GenericImpService;
import com.esthetic.reservations.model.Client;
import com.esthetic.reservations.repository.ClientRepository;

@Service
public class ClientService implements GenericImpService<Client> {
	
	@Autowired
	private ClientRepository usuarioRepository;

	@Override
	public Client save(Client usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public Client update(Client usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public List<Client> findAll() {
		return (List<Client>) usuarioRepository.findAll();
	}

	@Override
	public Client find(Integer id) {
		Optional<Client> obj = usuarioRepository.findById(id);
		if(obj.isPresent()) {
			return obj.get();
		}
		return null;
	}

	@Override
	public void delete(Integer id) {
		usuarioRepository.deleteById(id);
	}
	
	@Override
	public List<Client> where(String columna, String valor) {
		return (List<Client>) usuarioRepository.where(columna, valor);
	}
}
