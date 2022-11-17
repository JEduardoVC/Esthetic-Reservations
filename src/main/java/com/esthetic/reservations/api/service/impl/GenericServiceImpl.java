package com.esthetic.reservations.api.service.impl;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.GenericModelDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.BaseModel;
import com.esthetic.reservations.api.repository.GenericRepository;
import com.esthetic.reservations.api.service.GenericService;

@Service
public class GenericServiceImpl<T extends BaseModel<T>, D extends GenericModelDTO>
        implements GenericService<T, D> {

    private GenericRepository<T, Long> repository;
    private ModelMapper modelMapper;
    private Class<T> type;
    private Class<D> dtoType;

    public GenericServiceImpl() {
    }
    
    public GenericServiceImpl(GenericRepository<T, Long> repository, ModelMapper modelMapper,
            Class<T> type, Class<D> dtoType) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.type = type;
        this.dtoType = dtoType;
    }

    public GenericRepository<T, Long> getRepository() {
        return this.repository;
    }

    public void setRepository(GenericRepository<T, Long> repository) {
        this.repository = repository;
    }

    public ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Class<T> getType() {
        return this.type;
    }

    public Class<D> getDtoType() {
        return this.dtoType;
    }

    @Override
    public D mapToDTO(T t) {
        return getModelMapper().map(t, getDtoType());
    }

    @Override
    public T mapToModel(D dto) {
        return getModelMapper().map(dto, getType());
    }

    @Override
    public ResponseDTO<D> findAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<T> entities = getRepository().findAll(pageable);
        ArrayList<T> entitiesList = new ArrayList<>(entities.getContent());
        // To JSON list
        ArrayList<D> content = entitiesList.stream().map(entity -> mapToDTO(entity))
                .collect(Collectors.toCollection(ArrayList::new));
        ResponseDTO<D> userResponseDTO = new ResponseDTO<>();
        userResponseDTO.setContent(content);
        userResponseDTO.setPageNumber(pageNumber);
        userResponseDTO.setPageSize(pageSize);
        userResponseDTO.setCount(entities.getTotalElements());
        userResponseDTO.setTotalPages(entities.getTotalPages());
        userResponseDTO.setLast(entities.isLast());
        return userResponseDTO;
    }

    @Override
    public D findById(Long id) {
        T entity = getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getType().getSimpleName(), "no encontrado", "id",
                        String.valueOf(id)));
        return mapToDTO(entity);
    }

    @Override
    public D save(D dto) {
        T entity = mapToModel(dto);
        return mapToDTO(getRepository().save(entity));
    }

    @Override
    public D update(D dto, Long id) {
        T entity = getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(id)));
        T editedEntity = mapToModel(dto);
        entity.copy(editedEntity);
        getRepository().save(entity);
        return mapToDTO(entity);
    }

    @Override
    public void delete(Long id) {
        T entity = getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "no encontrado", "id", String.valueOf(id)));
        getRepository().delete(entity);
    }

}
