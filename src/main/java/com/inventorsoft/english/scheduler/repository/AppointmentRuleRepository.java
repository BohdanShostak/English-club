package com.inventorsoft.english.scheduler.repository;

import com.inventorsoft.english.scheduler.domain.AppointmentRule;
import com.inventorsoft.english.scheduler.domain.Occurrence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRuleRepository extends JpaRepository<AppointmentRule, Long> {

     void deleteAllByEnglishGroupId(Long groupId);

     void deleteAppointmentRuleByOccurrence(Occurrence occurrence);
}
