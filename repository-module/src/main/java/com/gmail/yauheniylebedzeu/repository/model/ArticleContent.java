package com.gmail.yauheniylebedzeu.repository.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "article_content")
@Data
public class ArticleContent {

    @GenericGenerator(name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "article"))
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "article_id")
    private Long articleId;

    @Column
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @ToString.Exclude
    private Article article;
}
