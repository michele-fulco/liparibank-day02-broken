package com.lipari.bank.account;

import com.lipari.bank.account.dto.AccountCreateRequest;
import com.lipari.bank.account.dto.AccountResponse;
import com.lipari.bank.account.model.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "API per la gestione dei conti correnti LipariBank")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @Operation(
            summary = "Lista conti correnti",
            description = "Restituisce la lista paginata di tutti i conti correnti."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista recuperata con successo"),
            @ApiResponse(responseCode = "401", description = "Non autenticato")
    })
    public ResponseEntity<Page<AccountResponse>> findAll(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(accountService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Dettaglio conto corrente", description = "Recupera i dettagli di un conto tramite ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conto trovato"),
            @ApiResponse(responseCode = "404", description = "Conto non trovato")
    })
    public ResponseEntity<AccountResponse> findById(
            @Parameter(description = "ID del conto corrente", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Creazione conto corrente", description = "Crea un nuovo conto corrente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conto creato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati non validi")
    })
    public ResponseEntity<AccountResponse> create(
            @Valid @RequestBody Account request
    ) {
        AccountCreateRequest dto = new AccountCreateRequest(null, request.getBalance());
        AccountResponse created = accountService.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aggiornamento conto corrente")
    public ResponseEntity<AccountResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AccountCreateRequest request
    ) {
        return ResponseEntity.ok(accountService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminazione conto corrente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conto eliminato"),
            @ApiResponse(responseCode = "404", description = "Conto non trovato")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
