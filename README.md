# Architecture Event Sourcing & CQRS avec Spring Boot, Axon Framework et Axon Server

## Objectif du projet

Ce projet met en œuvre de manière pratique les patterns d'architecture **Event Sourcing** et **CQRS (Command Query Responsibility Segregation)** avec **Spring Boot (Java 21)**, **Axon Framework** et **Axon Server**.

L'application développée est un système de **gestion de comptes bancaires** permettant de :
- Créer un compte bancaire et l'activer automatiquement
- Effectuer des opérations de **crédit** et de **débit** avec validation des règles métier
- Modifier le statut d'un compte (`CREATED`, `ACTIVATED`, `SUSPENDED`, `BLOCKED`)
- Consulter la liste des comptes ainsi que le relevé/historique des opérations d'un compte
- Suivre en **temps réel (SSE - Server-Sent Events)** les opérations effectuées sur un compte

---

## Architecture globale

L'application est structurée selon les principes **CQRS** et **Event Sourcing** :

1. **Côté Commande (Command Side)** :
   - Reçoit les requêtes d'écriture via `AccountCommandController` (`/commands/accounts/*`).
   - Transmet des commandes immuables (`AddAccountCommand`, `CreditAccountCommand`, `DebitAccountCommand`, `UpdateAccountStatusCommand`) via le `CommandGateway`.
   - L'agrégat `AccountAggregate` (`@Aggregate`) traite chaque commande dans des méthodes `@CommandHandler` (fonction de décision / règles métier) et publie des événements via `AggregateLifecycle.apply(...)` dans l'**Event Store**.
   - L'état interne de l'agrégat est mis à jour par les méthodes `@EventSourcingHandler` (fonction d'évolution).

2. **Événements (Events)** :
   - `AccountCreatedEvent`, `AccountActivatedEvent`, `AccountCreditedEvent`, `AccountDebitedEvent`, `AccountStatusUpdatedEvent`.
   - Sérialisés en **JSON** via Jackson (`axon.serializer.events=jackson`).

3. **Côté Lecture (Query Side)** :
   - `AccountEventHandler` (`@EventHandler`) écoute les événements publiés et projette les données dans la base relationnelle PostgreSQL (`Account` et `AccountOperation`).
   - `AccountQueryController` (`/query/accounts/*`) interroge le modèle de lecture via le `QueryGateway`.
   - `AccountQueryHandler` (`@QueryHandler`) répond aux requêtes `GetAllAccountsQuery`, `GetAccountStatementQuery` et `WatchEventQuery`.

4. **Streaming Temps Réel (SSE)** :
   - Utilisation de **Project Reactor** (`Flux<AccountOperation>`) et `QueryUpdateEmitter` pour diffuser en temps réel chaque nouvelle opération de débit ou de crédit sur l'endpoint `/query/accounts/watch/{accountId}`.

5. **Distribution avec Axon Server** :
   - Connexion gRPC (port `8124`) et dashboard Web (port `8024`) via le conteneur Docker `axoniq/axonserver`.

---

## Structure du projet

```text
src/main/java/com/example/demoescqrsaxon
├── commands
│   ├── aggregates
│   │   └── AccountAggregate.java
│   ├── commands
│   │   ├── AddAccountCommand.java
│   │   ├── CreditAccountCommand.java
│   │   ├── DebitAccountCommand.java
│   │   └── UpdateAccountStatusCommand.java
│   └── controllers
│       └── AccountCommandController.java
├── dtos
│   ├── AddNewAccountRequestDTO.java
│   ├── CreditAccountRequestDTO.java
│   ├── DebitAccountRequestDTO.java
│   └── UpdateAccountStatusRequestDTO.java
├── enums
│   ├── AccountStatus.java
│   └── OperationType.java
├── events
│   ├── AccountActivatedEvent.java
│   ├── AccountCreatedEvent.java
│   ├── AccountCreditedEvent.java
│   ├── AccountDebitedEvent.java
│   └── AccountStatusUpdatedEvent.java
├── query
│   ├── controllers
│   │   └── AccountQueryController.java
│   ├── dtos
│   │   └── AccountStatementResponseDTO.java
│   ├── entities
│   │   ├── Account.java
│   │   └── AccountOperation.java
│   ├── handlers
│   │   ├── AccountEventHandler.java
│   │   └── AccountQueryHandler.java
│   ├── queries
│   │   ├── GetAccountStatementQuery.java
│   │   ├── GetAllAccountsQuery.java
│   │   └── WatchEventQuery.java
│   └── repository
│       ├── AccountOperationRepository.java
│       └── AccountRepository.java
└── DemoEsCqrsAxonApplication.java
```

---

## Démarrage de l'infrastructure (Docker Compose)

Le fichier `docker-compose.yml` démarre :
- **PostgreSQL** (`localhost:5432`, base `accounts_db`)
- **PgAdmin 4** (`http://localhost:8088`)
- **Axon Server** (`http://localhost:8024` pour l'interface Web, `8124` pour gRPC)

```bash
docker compose up -d
```

---

## Exécution de l'application

```bash
./mvnw spring-boot:run
```

Interface Swagger UI disponible sur :
- `http://localhost:8066/swagger-ui.html`

---

## Principaux Endpoints REST

### Command Side (`/commands/accounts`)
- `POST /commands/accounts/add` : Créer un nouveau compte
- `POST /commands/accounts/credit` : Créditer un compte existant
- `POST /commands/accounts/debit` : Débiter un compte existant
- `PUT /commands/accounts/updateStatus` : Mettre à jour le statut d'un compte

### Query Side (`/query/accounts`)
- `GET /query/accounts/all` : Liste de tous les comptes projetés
- `GET /query/accounts/accountStatement/{accountId}` : Relevé complet d'un compte et historique de ses opérations
- `GET /query/accounts/watch/{accountId}` (`text/event-stream`) : Suivi en temps réel (SSE) des opérations sur un compte
