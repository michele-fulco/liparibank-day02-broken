package com.lipari.bank.account.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountResponse(
        Long id,
        String iban,
        BigDecimal balance,
        String status,
        String createdAt
) {}
