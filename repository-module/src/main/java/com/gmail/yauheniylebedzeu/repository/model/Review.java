package com.gmail.yauheniylebedzeu.repository.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "review")
@DynamicInsert
@Getter
@Setter
@EqualsAndHashCode(exclude = {"uuid", "additionDate", "user"})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uuid;

    @Column
    private String content;

    @Column(name = "addition_date")
    private LocalDate additionDate;

    @Column(name = "is_visible")
    private Boolean isVisible;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

}
