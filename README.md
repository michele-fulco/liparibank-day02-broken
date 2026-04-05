# LipariBank Day 02 — REST API & DTO Pattern

Benvenuto nel progetto **liparibank-day02-broken**.

Applicazione Spring Boot 3.3.4 (Java 21) con 5 endpoint REST per la gestione dei
conti correnti LipariBank. Il progetto compila e si avvia, ma contiene **3 bug intenzionali**
che impediscono il funzionamento corretto delle API.

Il tuo obiettivo è trovarli tutti e tre.

---

## Come avviare il progetto

```bash
./mvnw spring-boot:run
```

Su Windows:

```cmd
mvnw.cmd spring-boot:run
```

---

## Le 3 Missioni

### Missione 1 — Il POST accetta i dati sbagliati

L'endpoint `POST /api/v1/accounts` risulta accessibile e risponde, ma il body
atteso non corrisponde al contratto dichiarato. Swagger UI mostra uno schema
di input incoerente con il DTO progettato. Inviando un body corretto strutturato
per la creazione di un conto, la risposta è anomala o manca di campi chiave.

**Sintomo:** il body esempio in Swagger UI per il POST non mostra i campi
`codiceFiscale` e `initialBalance`, e il dato `codiceFiscale` non viene
registrato nel conto creato.

---

### Missione 2 — Le API sono irraggiungibili

Dopo l'avvio, tutti gli endpoint REST restituiscono un errore HTTP senza
nessun messaggio di errore applicativo nel body. Non importa se si usa
GET, POST, PUT o DELETE — la risposta è sempre la stessa, anche sugli
endpoint di sola lettura che non richiedono alcuna autenticazione.

**Sintomo:** ogni chiamata agli endpoint (incluso `GET /api/v1/accounts`)
restituisce un codice di errore HTTP senza che l'applicazione abbia
elaborato la richiesta.

---

### Missione 3 — Il sistema si avvia ma crasha alla prima richiesta

L'applicazione parte senza errori, il contesto Spring viene costruito
correttamente e i log di avvio sembrano normali. Ma alla prima chiamata
a qualsiasi endpoint, il server risponde con un errore interno e
nei log compare un `NullPointerException` all'interno di `AccountService`.

**Sintomo:** `NullPointerException` a runtime su `AccountService` durante
l'elaborazione di una richiesta, nonostante l'application context sia
stato creato senza errori.

---

## Come testare

### Con curl

```bash
# Lista conti
curl -X GET http://localhost:8080/api/v1/accounts

# Dettaglio conto
curl -X GET http://localhost:8080/api/v1/accounts/1

# Creazione conto
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{"codiceFiscale": "RSSMRA80A01H501Z", "initialBalance": 1000.00}'

# Aggiornamento conto
curl -X PUT http://localhost:8080/api/v1/accounts/1 \
  -H "Content-Type: application/json" \
  -d '{"codiceFiscale": "RSSMRA80A01H501Z", "initialBalance": 2000.00}'

# Eliminazione conto
curl -X DELETE http://localhost:8080/api/v1/accounts/1
```

### Con Postman

Importa una nuova collection e configura la variabile d'ambiente `baseUrl`
con valore `http://localhost:8080`. Usa `{{baseUrl}}/api/v1/accounts`
come URL base per tutte le request.

### Swagger UI

Disponibile a: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Specifica OpenAPI JSON: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

## Hint generale

> Usa `./mvnw spring-boot:run` e osserva attentamente i log di startup.
> Leggi ogni classe con attenzione — il codice sembra corretto a prima vista,
> ma il diavolo si nasconde nei dettagli.

---

## Struttura del progetto

```
src/main/java/com/lipari/bank/
├── LipariBankApplication.java
├── account/
│   ├── model/
│   │   └── Account.java
│   ├── dto/
│   │   ├── AccountCreateRequest.java
│   │   └── AccountResponse.java
│   ├── AccountRepository.java
│   ├── AccountMapper.java
│   ├── AccountService.java
│   └── AccountController.java
└── shared/
    └── config/
        └── OpenApiConfig.java
```

---

## Bonus Mission — Feature da Implementare (opzionale, ~1 ora)

Una volta risolti i 3 bug, implementa la seguente feature per consolidare i concetti del giorno.

### Ricerca conti con filtro per saldo

Il controller attuale espone solo operazioni CRUD base. Manca la possibilità di cercare i conti filtrando per range di saldo.

**Cosa implementare:**

Aggiungi un endpoint `GET /api/v1/accounts/search` che accetta i seguenti query parameter opzionali:

- `minBalance` — restituisce solo i conti con saldo maggiore o uguale al valore indicato
- `maxBalance` — restituisce solo i conti con saldo minore o uguale al valore indicato

I due parametri sono combinabili: si può passare solo uno dei due, entrambi, o nessuno. La risposta deve essere una lista di `AccountResponse` (non le entity JPA). Aggiungi al repository il metodo di query necessario e propagalo nel service prima di esporlo nel controller. L'endpoint deve comparire in Swagger UI con descrizione e schema corretto.

**Criteri di accettazione:**

- `GET /api/v1/accounts/search?minBalance=500` restituisce solo i conti con saldo >= 500.
- `GET /api/v1/accounts/search?maxBalance=2000` restituisce solo i conti con saldo <= 2000.
- `GET /api/v1/accounts/search?minBalance=500&maxBalance=2000` applica entrambi i filtri.
- `GET /api/v1/accounts/search` senza parametri restituisce tutti i conti.
- La risposta non contiene mai campi interni dell'entity che non fanno parte del DTO.
- L'endpoint è visibile e testabile da Swagger UI.

---

*LipariBank Prompt Bootcamp — REST API Design & Lombok — Day 02*
