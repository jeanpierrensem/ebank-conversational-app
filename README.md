# 🏦 E-Bank AI Assistant — Spring AI & Model Context Protocol (MCP)

Assistant bancaire conversationnel basé sur un **LLM** et le **Model Context Protocol (MCP)**.
Un microservice métier expose ses opérations bancaires sous forme d'**outils MCP** ; un agent IA les découvre et les appelle automatiquement pour répondre en langage naturel, via une **API REST** ou un **bot Discord**.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-brightgreen)
![Spring AI](https://img.shields.io/badge/Spring%20AI-2.0-blue)
![MCP](https://img.shields.io/badge/MCP-Streamable%20HTTP-purple)

---

## 📐 Architecture

```
┌──────────────┐        ┌────────────────────────────┐        ┌──────────────────────────┐
│  Utilisateur │        │     ebank-mcp-client       │  MCP   │      ebank-service       │
│              │──────▶ │        (port 8085)         │──────▶ │       (port 8787)        │
│  • Discord   │        │                            │ HTTP   │                          │
│  • REST /chat│ ◀──────│  EbankAgent (ChatClient)   │◀────── │  Serveur MCP (8 tools)   │
└──────────────┘        │  + OpenAI GPT-4o           │ /mcp   │  API REST + Swagger      │
                        │  + mémoire conversationnelle│        │  JPA / H2                │
                        └────────────────────────────┘        └──────────────────────────┘
```

| Module               | Rôle                                                                                                  |
| -------------------- | ----------------------------------------------------------------------------------------------------- |
| **ebank-service**    | Microservice métier : gestion des comptes et transactions. Expose une API REST **et** un serveur MCP. |
| **ebank-mcp-client** | Agent IA : client MCP + LLM (OpenAI). Exposé via REST et Discord.                                     |

---

## ✨ Fonctionnalités

### ebank-service — Serveur MCP

- Gestion de comptes (épargne / courant) multi-devises : **MAD, EUR, USD**
- Opérations : création de compte, **crédit**, **débit**, **virement** (transactionnel)
- Historisation de chaque opération (transaction avec UUID, date, type, statut)
- Exceptions métier : compte/transaction introuvable, montant négatif
- **8 outils MCP** exposés via `@McpTool` :

| Outil MCP                       | Description                             |
| ------------------------------- | --------------------------------------- |
| `getAccountById`                | Récupérer un compte par son id          |
| `getAllAccounts`                | Lister tous les comptes                 |
| `getTransactionById`            | Récupérer une transaction               |
| `getAllTransactions`            | Lister toutes les transactions          |
| `getAllTransactionsByAccountId` | Historique d'un compte                  |
| `saveAccount`                   | Créer un compte (solde initial, devise) |
| `credit`                        | Créditer un compte                      |
| `debit`                         | Débiter un compte                       |

### ebank-mcp-client — Agent IA

- `ChatClient` Spring AI connecté à **OpenAI GPT-4o**
- Découverte automatique des outils MCP (`ToolCallbackProvider`) → **tool calling**
- **Mémoire conversationnelle** (`MessageChatMemoryAdvisor`)
- Deux canaux d'accès : **API REST** et **bot Discord**

---

## 🛠️ Stack technique

- **Java 21**, **Maven**
- **Spring Boot 4.1** (Web MVC, Data JPA, Actuator)
- **Spring AI 2.0** : `ChatClient`, Tool Calling, Chat Memory, MCP Server & MCP Client
- **Model Context Protocol** — transport Streamable HTTP (JSON-RPC 2.0)
- **OpenAI GPT-4o**
- **Hibernate 7**, **H2** (base en mémoire)
- **Lombok**, **SpringDoc OpenAPI** (Swagger UI)
- **Discord** (`spring-boot-starter-discord` / JDA)

---

## 🚀 Démarrage

### Prérequis

- JDK 21
- Une clé API **OpenAI**
- _(optionnel)_ Un bot Discord ajouté à votre serveur, avec **Message Content Intent** activé

### 1. Configurer les secrets

Ne jamais committer de clés. Définissez-les en variables d'environnement :

```bash
export OPENAI_API_KEY=sk-...
export DISCORD_TOKEN=...
```

et référencez-les dans `ebank-mcp-client/src/main/resources/application.properties` :

```properties
spring.ai.openai.api-key=${OPENAI_API_KEY}
discord.token=${DISCORD_TOKEN}
```

### 2. Lancer le serveur MCP (en premier)

```bash
cd ebank-service
./mvnw spring-boot:run
```

### 3. Lancer l'agent IA

```bash
cd ebank-mcp-client
./mvnw spring-boot:run
```

> ⚠️ L'ordre compte : le client MCP se connecte au serveur au démarrage.

---

## 🔗 Points d'accès

| Service           | URL                                                                    |
| ----------------- | ---------------------------------------------------------------------- |
| Endpoint MCP      | `http://localhost:8787/mcp`                                            |
| API REST bancaire | `http://localhost:8787/accounts`, `/transactions`                      |
| Swagger UI        | `http://localhost:8787/swagger-ui.html`                                |
| Console H2        | `http://localhost:8787/h2-console` (JDBC URL : `jdbc:h2:mem:ebank-db`) |
| Chat avec l'agent | `http://localhost:8085/chat?query=...`                                 |

---

## 💬 Exemples d'utilisation

**Via REST :**

```bash
curl "http://localhost:8085/chat?query=Quel%20est%20le%20solde%20du%20compte%201%20?"
curl "http://localhost:8085/chat?query=Liste%20les%205%20dernieres%20transactions%20du%20compte%202"
curl "http://localhost:8085/chat?query=Cree%20un%20compte%20en%20EUR%20avec%201000%20de%20solde"
```

**Via Discord :** écrivez simplement dans un salon où le bot est présent :

> Quels sont tous les comptes en USD ?

---

## 🧪 Tester le serveur MCP

### Avec MCP Inspector

```bash
npx @modelcontextprotocol/inspector
```

- **Transport Type** : `Streamable HTTP`
- **URL** : `http://localhost:8787/mcp`

### Avec curl (handshake JSON-RPC)

```bash
curl -i -X POST http://localhost:8787/mcp \
  -H "Content-Type: application/json" \
  -H "Accept: application/json, text/event-stream" \
  -d '{"jsonrpc":"2.0","id":0,"method":"initialize","params":{"protocolVersion":"2025-11-25","capabilities":{},"clientInfo":{"name":"curl","version":"1"}}}'
```

La réponse contient un header `Mcp-Session-Id` à réutiliser pour les appels suivants (`tools/list`, `tools/call`).

---

## ⚙️ Configuration clé

**ebank-service**

```properties
server.port=8787
spring.ai.mcp.server.name=ebank-mcp-server
spring.ai.mcp.server.protocol=STREAMABLE
```

**ebank-mcp-client**

```properties
server.port=8085
spring.ai.openai.model=gpt-4o
spring.ai.mcp.client.streamable-http.connections.ebank-service.url=http://localhost:8787/mcp
```

---

## 📁 Structure

```
demo-ebank-app/
├── ebank-service/
│   └── src/main/java/com/jp/ebankservice/
│       ├── domain/        # Entités JPA & enums (Account, Transaction, Currency…)
│       ├── repository/    # Spring Data JPA
│       ├── services/      # Logique métier transactionnelle
│       ├── rest/          # API REST
│       ├── mcp/           # Outils MCP (@McpTool)
│       └── Exceptions/    # Exceptions métier
└── ebank-mcp-client/
    └── src/main/java/com/jp/ebankmcpclient/
        ├── agent/         # EbankAgent (ChatClient + tools MCP + mémoire)
        ├── rest/          # Endpoint /chat
        └── discord/       # Bot Discord
```

---

## 📚 Concepts abordés

- Architecture **agentique** : un LLM orchestre des appels à un backend métier
- **MCP** : serveur, client, transports (STDIO / SSE / Streamable HTTP), négociation de capacités
- **Tool calling** et **mémoire conversationnelle** avec Spring AI
- Microservices Spring Boot, JPA, transactions, gestion d'exceptions métier

## 👤 Auteur

**[Jean-Pierre NSEM]** — Master 2 · [https://www.linkedin.com/in/jeanpierre-nsem-chef-de-projet-it/]
