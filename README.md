# 🧩 Form Workflow Engine

A configurable and extensible **Form Workflow Engine** built using **Spring Boot**, **PostgreSQL**, and **Keycloak** for authentication and role-based authorization.  
This project  showcases a real-world, production-quality implementation of dynamic forms and workflow management.

---
## ✨ Postman Collection - [URL](https://orange-crescent-418337.postman.co/workspace/birth-reg~9e92836c-7e82-44be-a3d1-0d263fba06fe/collection/35025473-d656cf6d-e586-4ebd-b574-c8085993d0cd?action=share&source=copy-link&creator=35025473)


---

## ✨ Features

- 🔐 **Authentication & Authorization** via Keycloak (JWT + Roles)
- 📄 Dynamic **Form Submission API** (user-defined schema support)
- 🔄 Configurable **Workflow Engine** (multi-state, role-based transitions)
- 🧠 Business-Service-driven architecture (define states, actions, roles)
- 📜 View current state & full transition history of any form
- 🌐 Multi-tenant architecture support
- ☁️ Designed with clean separation: Controller → Service → Repository layers

---

## 🏗️ Tech Stack

| Layer               | Technology                   |
|--------------------|------------------------------|
| Backend Framework  | Spring Boot 3.x              |
| Auth Provider      | Keycloak                     |
| Data Store         | PostgreSQL                   |
| Token Parsing      | `jjwt` + public key (JWKS)   |
| Build Tool         | Maven                        |
| Infra Support      | Docker (optional)            |

---

## 🔑 Key Concepts

- **BusinessService**: Defines a workflow (states, actions, SLA, roles).
- **WorkflowInstance**: Runtime representation of a form moving through workflow.
- **FormData**: Dynamic user-submitted form stored as JSON (`Map<String, Object>`).
- **WorkflowHistory**: Auditable history of transitions with timestamp and actor.
- **Role Matching**: Transition allowed only if user's Keycloak roles match allowed roles in workflow config.

---

## 🚀 How to Run Locally

### 🔧 Requirements

- Java 17+
- PostgreSQL (with a schema created)
- Maven
- Keycloak (local or cloud)

### 🔌 PostgreSQL Setup

```sql
CREATE DATABASE formworkflow;
```

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/formworkflow
spring.datasource.username=<username>
spring.datasource.password=<password>
```

### 🔐 Keycloak Setup

1. Create a realm `form-engine`
2. Create roles: `ADMIN`, `MANAGER`, `EMPLOYEE`, etc.
3. Create users and assign roles
4. Get the JWKS endpoint:  
   `http://localhost:8080/realms/form-engine/protocol/openid-connect/certs`

Update `application.properties`:

```properties
keycloak.jwks-url=http://localhost:8080/realms/form-engine/protocol/openid-connect/certs
```

---

## 📮 API Overview

### 🔹 Submit Form (Public)

```bash
curl -X POST http://localhost:8280/forms/data/_submit   -H 'Content-Type: application/json'   -d '{
        "formType": "leave-request",
        "businessService": "LEAVE_FLOW",
        "tenantId": "pb",
        "data": {
            "startDate": "2025-07-01",
            "endDate": "2025-07-03",
            "reason": "Personal"
        },
        "submittedBy": "user-uuid"
      }'
```

---

### 🔹 Transition Workflow (JWT Required)

```bash
curl -X POST http://localhost:8280/wf/process/_transition   -H 'Authorization: Bearer <access-token>'   -H 'Content-Type: application/json'   -d '{
        "RequestInfo": {
            "userInfo": {
                "uuid": "user-uuid",
                "roles": []
            }
        },
        "processInstances": [{
            "tenantId": "pb",
            "businessService": "LEAVE_FLOW",
            "formInstanceId": "<form-id>",
            "action": "APPROVE",
            "assignees": ["next-user-uuid"],
            "comment": "Approved by manager"
        }]
      }'
```

---

### 🔹 Get Current State

```bash
GET /wf/process/instance/{formId}?tenantId=pb
```

---

### 🔹 Get Transition History

```bash
GET /wf/process/history/{formId}?tenantId=pb
```
