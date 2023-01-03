package com.inventorsoft.english.groups.controller;

import com.inventorsoft.english.groups.domain.dto.GroupDto;
import com.inventorsoft.english.groups.service.GroupManagementFacade;
import com.inventorsoft.english.groups.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
@Slf4j
public class GroupRestController {

    private final GroupService groupService;

    private final GroupManagementFacade groupManagementFacade;

    @GetMapping
    public Page<GroupDto> getAllGroups(@PageableDefault(sort = {"name"},
            direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("getAllGroups");
        return groupService.listGroups(pageable);
    }

    @GetMapping("/mentor/{id}")
    public List<GroupDto> getAllGroupsByMentor(@PathVariable long id) {
        log.info("getAllGroups");
        return groupService.listGroupsByMentor(id);
    }

    @GetMapping("/{id}")
    public GroupDto getGroupById(@PathVariable long id) {
        log.info("getGroup by id {}", id);
        return groupService.getGroup(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GroupDto createGroup(@Valid @RequestBody GroupDto groupDto) {
        log.info("createGroup");
        return groupManagementFacade.createGroup(groupDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public GroupDto updateGroup(@PathVariable Long id, @Valid @RequestBody GroupDto groupDto) {
        log.info("updateGroup by id {}", id);
        return groupService.updateGroup(id, groupDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        log.info("deleteAccount by id {}", id);
        groupManagementFacade.deleteGroup(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{groupId}/user/{userId}")
    public void addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupManagementFacade.addUserToGroup(userId, groupId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user/{userId}")
    public void deleteUserFromGroup(@PathVariable Long userId) {
        groupService.deleteUser(userId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/group/{groupId}/mentor/{mentorId}")
    public void addMentorToGroup(@PathVariable Long groupId, @PathVariable Long mentorId) {
        groupService.addMentor(mentorId, groupId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/group/{groupId}")
    public void deleteMentorFromGroup(@PathVariable Long groupId) {
        groupService.deleteMentor(groupId);
    }

}
