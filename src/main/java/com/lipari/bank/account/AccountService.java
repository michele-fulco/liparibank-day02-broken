package com.lipari.bank.account;

import com.lipari.bank.account.dto.AccountCreateRequest;
import com.lipari.bank.account.dto.AccountResponse;
import com.lipari.bank.account.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public Page<AccountResponse> findAll(Pageable pageable) {
        log.debug("Recupero lista conti - pagina: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return accountRepository.findAll(pageable).map(accountMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public AccountResponse findById(Long id) {
        log.debug("Recupero conto con id: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conto non trovato con id: " + id));
        return accountMapper.toResponse(account);
    }

    @Transactional
    public AccountResponse create(AccountCreateRequest request) {
        log.info("Creazione nuovo conto per codice fiscale: {}", request.codiceFiscale());
        Account account = accountMapper.toEntity(request);
        account.setIban("IT" + UUID.randomUUID().toString().replace("-", "").substring(0, 24).toUpperCase());
        Account saved = accountRepository.save(account);
        log.info("Conto creato con id: {} e IBAN: {}", saved.getId(), saved.getIban());
        return accountMapper.toResponse(saved);
    }

    @Transactional
    public AccountResponse update(Long id, AccountCreateRequest request) {
        log.info("Aggiornamento conto con id: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conto non trovato con id: " + id));
        account.setBalance(request.initialBalance());
        return accountMapper.toResponse(account);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Eliminazione conto con id: {}", id);
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Conto non trovato con id: " + id);
        }
        accountRepository.deleteById(id);
    }
}
