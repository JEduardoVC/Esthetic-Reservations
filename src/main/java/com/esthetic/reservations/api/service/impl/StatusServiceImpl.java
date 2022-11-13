package com.esthetic.reservations.api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.StatusDTO;
import com.esthetic.reservations.api.model.Status;
import com.esthetic.reservations.api.repository.StatusRepository;
import com.esthetic.reservations.api.service.StatusService;

@Service
public class StatusServiceImpl extends GenericServiceImpl<Status, StatusDTO>
        implements StatusService {

    @Autowired
    public StatusServiceImpl(StatusRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, Status.class, StatusDTO.class);
        // TODO Auto-generated constructor stub
    }

}
