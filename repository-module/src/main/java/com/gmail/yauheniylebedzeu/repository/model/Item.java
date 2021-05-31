package com.gmail.yauheniylebedzeu.repository.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
@SQLDelete(sql = "UPDATE item SET is_deleted = 1 WHERE id = ?")
@DynamicInsert
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uuid;

    @Column
    @EqualsAndHashCode.Exclude
    private String name;

    @Column
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantityInStock;

    @Column(name = "copy_number")
    private Integer copyNumber;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "item",
            orphanRemoval = true)
    private ItemDescription itemDescription;

    @OneToMany(mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CartDetail> cartDetails = new HashSet<>();

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public void incrementCopyNumber() {
        copyNumber++;
    }
}