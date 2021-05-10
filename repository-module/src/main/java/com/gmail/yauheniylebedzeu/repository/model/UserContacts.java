package com.gmail.yauheniylebedzeu.repository.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_contacts")
@SQLDelete(sql = "UPDATE user_contacts SET is_deleted = 1 WHERE user_id = ?")
@Where(clause = "is_deleted = 0")
@DynamicInsert
@DynamicUpdate
@Data
public class UserContacts {

    @GenericGenerator(name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "user"))
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "user_id")
    private Long userId;

    @Column
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_deleted")
    @EqualsAndHashCode.Exclude
    private Boolean isDeleted;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
}
