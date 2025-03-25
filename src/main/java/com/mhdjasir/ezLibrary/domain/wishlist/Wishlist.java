package com.mhdjasir.ezLibrary.domain.wishlist;

import com.mhdjasir.ezLibrary.domain.book.Book;
import com.mhdjasir.ezLibrary.domain.model.BaseEntity;
import com.mhdjasir.ezLibrary.domain.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "\"wishlist\"")
public class Wishlist extends BaseEntity {
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

    @Column(name = "book_id")
    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "book_id",referencedColumnName = "id",insertable = false,updatable = false)
    private Book book;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",insertable = false,updatable = false)
    private User user;

}
