package com.mhdjasir.ezLibrary.domain.order;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateOrderRequestDTO {

    @NotBlank(message = "Order status is required")
    private String orderStatus;
}
