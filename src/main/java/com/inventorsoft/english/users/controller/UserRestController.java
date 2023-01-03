package com.inventorsoft.english.users.controller;

import com.inventorsoft.english.general.principal.CurrentUserProvider;
import com.inventorsoft.english.users.domain.dto.UserDto;
import com.inventorsoft.english.users.domain.model.Role;
import com.inventorsoft.english.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;


    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getUserDtoById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public Page<UserDto> getAll(@PageableDefault(sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable) {
        return userService.getAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PatchMapping("/{id}")
    public UserDto updateProfile(@PathVariable Long id,
                              @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @GetMapping("/group/{id}")
    public Page<UserDto> getAllByGroup(@PathVariable Long id, @PageableDefault(sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable) {
        return userService.getAllByGroup(id, pageable);
    }

}
