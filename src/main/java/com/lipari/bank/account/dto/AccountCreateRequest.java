package com.lipari.bank.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountCreateRequest(

        @NotBlank(message = "Il codice fiscale del titolare è obbligatorio")
        String codiceFiscale,

        @NotNull(message = "Il saldo iniziale è obbligatorio")
        @DecimalMin(value = "0.00", message = "Il saldo iniziale non può essere negativo")
        BigDecimal initialBalance

) {}
