
# 🧩 Configurable Form and Workflow Engine

A lightweight, modular backend system that allows:
- Admins to define dynamic forms
- Admins to configure state-machine-based workflows
- Users to submit forms and trigger workflows
- Role-based workflow transitions via Keycloak authentication

---

## 🚀 Problem Statement

Organizations often need customizable processes like leave applications, approvals, or expense reimbursements. Instead of hardcoding each workflow, this system enables:
- Dynamic form creation using JSON schema
- Configurable workflows via state machines
- Workflow execution driven by form submissions
- Role-based transitions, all managed via Keycloak

---
## ✨ Postman Collection - [URL](https://orange-crescent-418337.postman.co/workspace/birth-reg~9e92836c-7e82-44be-a3d1-0d263fba06fe/collection/35025473-d656cf6d-e586-4ebd-b574-c8085993d0cd?action=share&source=copy-link&creator=35025473)

## 🧠 Design Approach

### 🔧 1. Form Configuration
- Admins can POST form schemas via `/forms/config/_create`.
- Schema format:
```json
{
  "schemaCode": "leave-application",
  "fields": [
    { "name": "reason", "type": "text", "required": true },
    { "name": "from_date", "type": "date" },
    { "name": "to_date", "type": "date" }
  ]
}
```

---

### 🔄 2. Workflow Configuration
- Admins define workflows via `/wf/businessservice/_create`.
- Workflow is modeled as a `BusinessService` with:
  - States (`StateDefinition`)
  - Actions (`ActionDefinition`) containing allowed roles and next state.

Example:
```json
{
  "businessService": "leave-workflow",
  "business": "form-workflow",
  "states": [
    {
      "state": "Draft",
      "isStartState": true,
      "actions": [
        {
          "action": "SUBMIT",
          "nextState": "Review",
          "roles": ["ROLE_EMPLOYEE"]
        }
      ]
    },
    {
      "state": "Review",
      "actions": [
        {
          "action": "APPROVE",
          "nextState": "Approved",
          "roles": ["ROLE_MANAGER"]
        }
      ]
    }
  ]
}
```

---

### 📝 3. Form Submission & Workflow Execution
- Endpoint: `/forms/data/_submit`
- Validates input using form schema
- Creates a `WorkflowInstance` in the start state
- Each submission is associated with a unique workflow instance

---

### 🔄 4. Workflow Transition
- Endpoint: `/wf/process/_transition`
- Authenticated users (via Keycloak JWT) can perform allowed actions
- Role-based authorization validated against `ActionDefinition.roles`

---

### 🔐 5. Authentication & Role Management
- Integrated with **Keycloak**:
  - JWT tokens are validated using JWKS URL
  - Roles extracted from `realm_access.roles`
  - Used with Spring Security and custom `JwtAuthenticationFilter`
- Role mapping follows Spring convention: `ROLE_MANAGER`, `ROLE_EMPLOYEE`, etc.

---

## ⚙️ Tech Stack

- Java 17 + Spring Boot 3.x
- PostgreSQL (Database)
- Spring Security (Stateless, JWT)
- Keycloak (Identity Provider)
- Docker (optional)

---

## 📂 Project Modules

```
form-workflow-engine/
├── config/                 # Spring + Keycloak configuration
├── controller/             # REST APIs
├── dto/                    # Request/Response models
├── model/                  # JPA entities (Form, WorkflowInstance, State, etc.)
├── repository/             # Spring Data Repos
├── security/               # JWT Filter, JWKS decoding
├── service/                # Business logic
└── application.properties
```

---

## 🔧 Setup Instructions

### ✅ Prerequisites
- Java 17+
- PostgreSQL running on port `5432`
- Keycloak setup with:
  - Realm (e.g., `workflow`)
  - Client configured with `RS256`
  - Roles: `EMPLOYEE`, `MANAGER`, `ADMIN`, etc.

### 🔑 Application Properties
```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:5432/workflow
spring.datasource.username=<username>
spring.datasource.password=<password>

keycloak.jwks-url=http://localhost:8080/realms/workflow/protocol/openid-connect/certs
```

### 🧪 Run the App
```bash
./mvnw spring-boot:run
```



---

## 🔐 Keycloak Setup (Minimal)
1. Create Realm: `workflow`
2. Create Client: `workflow-client` (confidential, access type = bearer-only)
3. Add roles: `EMPLOYEE`, `MANAGER`, `HR`, `ADMIN`
4. Create users with assigned roles
5. Get token:
```bash
curl -X POST \
  http://localhost:8080/realms/workflow/protocol/openid-connect/token \
  -d "grant_type=password" \
  -d "client_id=workflow-client" \
  -d "client_secret=workflow-client" \
  -d "username=employee1" \
  -d "password=123456"
```

---

## 🧪 API Overview

| Action | Endpoint | Auth Required | Role Required |
|--------|----------|---------------|----------------|
| Submit Form | `/forms/data/_submit` | ❌ No | - |
| Create Form Config | `/forms/config/_create` | ✅ Yes | `ADMIN` |
| Create Workflow Config | `/wf/businessservice/_create` | ✅ Yes | `ADMIN` |
| Transition Workflow | `/wf/process/_transition` | ✅ Yes | Based on state |
| Get Workflow Instance | `/wf/process/instance/{formId}` | ❌ No | - |
| Get Workflow History | `/wf/process/history/{formId}` | ❌ No | - |

---

## 📌 Assumptions

- Admins use Postman or Admin UI to configure forms/workflows
- User roles are provisioned in Keycloak only
- No frontend included (optional React/Next UI possible)
- All validations are done manually using schema JSON
- Stateless JWT-only auth

---

## 📚 Future Improvements

- UI using React or Angular
- Notifications on state transitions
- WebSocket integration for live updates
- Versioning of form schemas
- Exportable audit logs

---


