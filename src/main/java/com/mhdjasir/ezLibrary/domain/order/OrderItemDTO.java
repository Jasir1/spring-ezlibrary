package com.mhdjasir.ezLibrary.domain.order;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemDTO {

    @NotNull
    private Long bookId;

    @Positive
    private Integer qty;

    @Positive
    private Double price;

}
