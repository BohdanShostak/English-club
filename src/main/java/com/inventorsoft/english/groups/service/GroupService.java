package com.inventorsoft.english.groups.service;

import com.inventorsoft.english.groups.domain.dto.GroupDto;
import com.inventorsoft.english.groups.domain.mapper.GroupMapper;
import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import com.inventorsoft.english.groups.repository.GroupsRepository;
import com.inventorsoft.english.scheduler.domain.AppointmentRule;
import com.inventorsoft.english.scheduler.service.AppointmentRuleResolver;
import com.inventorsoft.english.scheduler.service.AppointmentRuleService;
import com.inventorsoft.english.users.domain.model.Role;
import com.inventorsoft.english.users.domain.model.User;
import com.inventorsoft.english.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {

    private final GroupsRepository groupsRepository;

    private final UserRepository userRepository;
    private final AppointmentRuleService appointmentRuleService;
    private final AppointmentRuleResolver appointmentRuleResolver;

    private final GroupMapper groupMapper = GroupMapper.INSTANCE;

    @Transactional(readOnly = true)
    public Optional<EnglishGroup> findById(Long id) {
        return groupsRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public GroupDto getGroup(long id) {
        log.info("Finding Group by {} id...", id);
        EnglishGroup group = groupsRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Group with id = " + id + " not found!"));
        log.info("Group with {} id is found", id);
        return groupMapper.mapModelToDto(group);
    }

    @Transactional(readOnly = true)
    public Page<GroupDto> listGroups(Pageable pageable) {
        log.info("Get all groups");
        return groupsRepository
                .findAll(pageable)
                .map(groupMapper::mapModelToDto);
    }

    @Transactional(readOnly = true)
    public List<GroupDto> listGroupsByMentor(long id) {
        log.info("Get all groups where mentorId {}", id);
        List<EnglishGroup> groupsByMentor = groupsRepository.findAllByMentorId(id);
        return groupMapper.mapListOfGroupToListOfDto(groupsByMentor);
    }

    @Transactional
    public EnglishGroup create(GroupDto groupDto) {
        EnglishGroup entity = groupMapper.mapDtoToModel(groupDto);
        return groupsRepository.save(entity);
    }

    @Transactional
    public GroupDto createGroup(GroupDto groupDto) {
        log.info("Creating group");
        AppointmentRule rule = appointmentRuleService.save(groupDto.getRule());
        EnglishGroup group = groupMapper.mapDtoToModel(groupDto);
        group.setRule(rule);
        groupsRepository.save(group);
        rule.setEnglishGroup(group);
        appointmentRuleService.save(rule);

        appointmentRuleResolver.resolveRule(rule);
        log.info("Group successfully created");
        return groupMapper.mapModelToDto(group);
    }

    @Transactional
    public GroupDto updateGroup(long id, GroupDto groupDto) {
        log.info("Updating group with {} id...", id);
        EnglishGroup persistedGroup = groupsRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Group with id = " + id + " not found!"));
        groupMapper.updateGroupFromDto(persistedGroup, groupDto);
        EnglishGroup storedGroup = groupsRepository.save(persistedGroup);
        log.info("Group with id {} successfully updated", storedGroup.getId());
        return groupMapper.mapModelToDto(persistedGroup);
    }

    @Transactional
    public void deleteGroup(long id) {
        appointmentRuleService.deleteByGroup(id);
        groupsRepository.deleteById(id);
        log.info("Group with id {} was deleted", id);
    }

    @Transactional
    public void addUser(Long userId, Long groupId) {
        log.info("Finding User by {} id...", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        EnglishGroup group = groupsRepository.findById(groupId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Group with id = " + groupId + " not found!"));
        log.info("Group with {} id is found", groupId);
        user.setEnglishGroup(group);
        userRepository.save(user);
        log.info("User with id {} was added to Group with id {}", userId, groupId);
    }

    @Transactional
    public void deleteUser(Long userId) {
        log.info("Finding User by {} id...", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        user.setEnglishGroup(null);
        userRepository.save(user);
        log.info("User with id {} removed from Group", userId);
    }

    @Transactional
    public void addMentor(Long mentorId, Long groupId) {
        log.info("Finding Mentor by {} id...", mentorId);
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        if (mentor.getRole().equals(Role.ROLE_ADMIN)) {
            EnglishGroup group = groupsRepository.findById(groupId)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Group with id = " + groupId + " not found!"));
            log.info("Group with {} id is found", groupId);
            group.setMentor(mentor);
            groupsRepository.save(group);
            log.info("Mentor with id {} was added to Group with id {}", mentorId, groupId);
        } else {
            throw new RuntimeException("This user can not be mentor in group!!!");
        }
    }

    @Transactional
    public void deleteMentor(Long groupId) {
        log.info("Finding Group by {} id...", groupId);
        EnglishGroup group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found!"));
        group.setMentor(null);
        log.info("Mentor was removed from Group with id {}", group);
    }

}
