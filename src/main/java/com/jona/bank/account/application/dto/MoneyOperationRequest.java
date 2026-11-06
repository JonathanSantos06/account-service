package com.jona.bank.account.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MoneyOperationRequest(

        @NotNull(message = "El importe es obligatorio")
        @DecimalMin(
                value = "0.00",
                inclusive = false,
                message = "El importe debe ser mayor que cero"
        )
        BigDecimal amount


) {
}