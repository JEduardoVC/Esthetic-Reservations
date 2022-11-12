package com.esthetic.reservations.service;

public interface GenericService<T, D, R> {

	public R findAll(int pageNumber, int pageSize, String sortBy, String sortDir);

	public D findById(Long id);

	public D save(D d);

	public D update(D d, Long id);

	public void delete(Long id);

}
