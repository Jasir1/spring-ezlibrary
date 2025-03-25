package com.mhdjasir.ezLibrary.domain.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookDTO {
    @NotBlank
    private String title;

    @NotNull
    private Long categoryId;

    @NotBlank
    private String author;

    @NotBlank
    private String publisher;

    @NotBlank
    private String publishedDate;

    @NotBlank
    private String isbn;

    @NotBlank
    private String language;

    @NotNull
    private Integer pageCount;

    @NotNull
    private Double price;

    @NotBlank
    private String description;

    @NotNull
    private Integer stock;

    private String condition;

}
