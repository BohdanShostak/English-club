package com.inventorsoft.english.classrequest.domain.model;


import com.inventorsoft.english.userrequest.model.UserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "class_request")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClassRequest extends UserRequest {
}
