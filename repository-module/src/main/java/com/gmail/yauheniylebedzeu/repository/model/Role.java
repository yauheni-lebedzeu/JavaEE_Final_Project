package com.gmail.yauheniylebedzeu.repository.model;

import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
}
