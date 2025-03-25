package com.mhdjasir.ezLibrary.domain.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mhdjasir.ezLibrary.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "\"category\"")
public class Category extends BaseEntity {
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

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "status")
//    @JsonIgnore
    private Boolean status = true;
}
