package com.lipari.bank.account;

import com.lipari.bank.account.dto.AccountCreateRequest;
import com.lipari.bank.account.dto.AccountResponse;
import com.lipari.bank.account.model.Account;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AccountMapper {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Account toEntity(AccountCreateRequest request) {
        Account account = new Account();
        account.setBalance(request.initialBalance());
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());
        return account;
    }

    public AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .iban(account.getIban())
                .balance(account.getBalance())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt() != null
                        ? account.getCreatedAt().format(ISO_FORMATTER)
                        : null)
                .build();
    }
}
