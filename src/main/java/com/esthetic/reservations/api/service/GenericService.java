package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.model.BaseModel;

public interface GenericService<T extends BaseModel<T>, D> {

	public D mapToDTO(T t);

	public T mapToModel(D dto);

	public ResponseDTO<D> findAll(int pageNumber, int pageSize, String sortBy, String sortDir);

	public D findById(Long id);

	public D save(D dto);

	public D update(D dto, Long id);

	public void delete(Long id);
}
