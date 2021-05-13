package com.gmail.yauheniylebedzeu.repository.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @EqualsAndHashCode.Exclude
    private String uuid;

    @Column
    @EqualsAndHashCode.Exclude
    private String name;

    @Column
    private BigDecimal price;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "item",
            orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private ItemDescription itemDescription;

}
