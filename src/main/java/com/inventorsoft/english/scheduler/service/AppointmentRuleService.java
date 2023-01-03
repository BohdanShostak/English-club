package com.inventorsoft.english.scheduler.service;

import com.inventorsoft.english.groups.repository.GroupsRepository;
import com.inventorsoft.english.scheduler.domain.AppointmentRule;
import com.inventorsoft.english.scheduler.domain.Occurrence;
import com.inventorsoft.english.scheduler.domain.dto.AppointmentRuleDto;
import com.inventorsoft.english.scheduler.domain.mapper.AppointmentRuleMapper;
import com.inventorsoft.english.scheduler.repository.AppointmentRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentRuleService {

    private final AppointmentRuleRepository appointmentRuleRepository;
    private final GroupsRepository groupsRepository;
    private final AppointmentRuleMapper mapper;

    @Transactional(readOnly = true)
    public List<AppointmentRule> getRules() {
        return appointmentRuleRepository.findAll();
    }

    @Transactional
    public AppointmentRule save(AppointmentRule rule) {
        return appointmentRuleRepository.save(rule);
    }

    @Transactional
    public AppointmentRule save(AppointmentRuleDto dto) {
        return appointmentRuleRepository.save(mapper.toEntity(dto));
    }

    @Transactional
    public void deleteNonRecurringRules() {
        appointmentRuleRepository.deleteAppointmentRuleByOccurrence(Occurrence.ONCE);
    }

    @Transactional
    public void deleteByGroup(Long groupId) {
        appointmentRuleRepository.deleteAllByEnglishGroupId(groupId);
    }
}
