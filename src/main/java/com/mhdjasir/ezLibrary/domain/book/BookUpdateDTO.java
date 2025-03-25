package com.mhdjasir.ezLibrary.domain.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookUpdateDTO {
    private String title;
    private Long categoryId;
    private String author;
    private String publisher;
    private String publishedDate;
    private String isbn;
    private String language;
    private Integer pageCount;
    private Double price;
    private String description;
    private Integer stock;
    private String condition;

//    @NotBlank
//    private String imagePath;


}
