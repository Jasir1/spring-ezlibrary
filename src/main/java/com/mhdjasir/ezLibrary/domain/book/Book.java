package com.mhdjasir.ezLibrary.domain.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mhdjasir.ezLibrary.domain.bookImage.BookImage;
import com.mhdjasir.ezLibrary.domain.category.Category;
import com.mhdjasir.ezLibrary.domain.model.BaseEntity;
import com.mhdjasir.ezLibrary.domain.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "\"book\"")
public class Book extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(name = "name", nullable = false)
    private String title;

    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id",updatable = false,insertable = false)
    private Category category;

    @Column(name = "author",nullable = false)
    private String author;

    @Column(name = "publisher",nullable = false)
    private String publisher;

    @Column(name = "published_date",nullable = false)
    private String publishedDate;

    @Column(name = "isbn",nullable = false)
    private String isbn;

    @Column(name = "language", nullable = false)
    @Enumerated(EnumType.STRING)
    private Language language = Language.ENGLISH;

    @Column(name = "page_count",nullable = false)
    private Integer pageCount = 0;

    @Column(name = "price",nullable = false)
    private Double price = 0.0;

    @Column(name = "description", columnDefinition = "TEXT",nullable = false)
    private String description;

    @Column(name = "stock",nullable = false)
    private Integer stock = 0;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",insertable = false,updatable = false)
    private User user;

    @Column(name = "ratings")
    private Double ratings = 0.0;

    @Column(name = "num_of_reviews")
    private Integer numOfReviews = 0;

    @Column(name = "condition")
    @Enumerated(EnumType.STRING)
    private Condition condition = Condition.USED;

    @Column(name = "delete")
    @JsonIgnore
    private Boolean delete = false;

    @Column(name = "deleted_at")
    @JsonIgnore
    private LocalDateTime deletedAt;

    @Column(name = "image_path")
    private String imagePath;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookImage> images = new ArrayList<>();
}
