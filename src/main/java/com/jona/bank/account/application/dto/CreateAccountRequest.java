
package com.jona.bank.account.application.dto;

import com.jona.bank.account.domain.model.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateAccountRequest(

        @NotBlank(message = "El customerId es obligatorio")
        String customerId,

        @NotNull(message = "El tipo de cuenta es obligatorio")
        AccountType accountType,

        @NotBlank(message = "La moneda es obligatoria")
        @Pattern(
                regexp = "MXN|USD",
                message = "La moneda debe ser MXN o USD"
        )
        String currency
) {
}
