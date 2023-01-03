package com.inventorsoft.english.userrequest.service;

import com.inventorsoft.english.userrequest.dto.UserRequestDto;

import java.util.List;


public abstract class UserRequestService<T extends UserRequestDto> {

    public abstract T findById(Long id);

    public abstract List<T> getAll();

    public abstract void deleteRequestById(Long id);

    public abstract List<T> allUserRequests(Long id);

    public abstract T rejectRequest(Long id);

    public abstract T createRequest(T dto);

    public abstract T confirmRequest(Long id, T dto);

}
