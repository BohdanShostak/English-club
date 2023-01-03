package com.inventorsoft.english.testrequest.domain.model;

import com.inventorsoft.english.general.domain.EnglishLevel;
import com.inventorsoft.english.userrequest.model.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "test_request")
@AllArgsConstructor
public class TestRequest extends UserRequest {

    @Enumerated(EnumType.STRING)
    @Column(name = "english_level")
    private EnglishLevel result;
}
