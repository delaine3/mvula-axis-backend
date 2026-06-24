# Mvula Axis Backend

Mvula Axis Backend is a Spring Boot API for managing vendor orders and order items.

It currently supports order creation, retrieval, updating, deletion, pagination, sorting, text search, and item-level changes inside an order.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Validation
- PostgreSQL
- Gradle
- Lombok

## Current Features

- Health check endpoint
- Create, read, update, patch, and delete orders
- Add, update, and remove order items
- Automatic order total calculation
- Pagination and sorting for order lists
- Search across vendor, description, and status
- Basic CORS configuration for a local frontend running on `http://localhost:5173`

## Project Structure

```txt
src/main/java/com/mvula/axis
├── common
│   ├── dto
│   └── exception
├── config
│   └── SecurityConfig.java
├── controller
│   └── HelloController.java
├── order
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   ├── service
│   └── specification
└── MvulaAxisApplication.java
```

## Getting Started

### Prerequisites

Install:

- Java 21
- PostgreSQL
- Gradle, or use the included Gradle wrapper

## Configuration

Create `src/main/resources/application.properties` and add your local database settings.

```properties
spring.application.name=mvula-axis

spring.datasource.url=jdbc:postgresql://localhost:5432/mvula_axis
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Create the database in PostgreSQL:

```sql
CREATE DATABASE mvula_axis;
```

## Run the App

From the project root:

```bash
./gradlew bootRun
```

On Windows:

```bash
gradlew.bat bootRun
```

The API runs at:

```txt
http://localhost:8080
```

## Health Check

```http
GET /hello
```

Response:

```txt
Mvula Axis backend is running
```

## Orders API

### Create an Order

```http
POST /orders
Content-Type: application/json
```

```json
{
  "vendor": "Acme Supplies",
  "description": "Office supplies order",
  "status": "PENDING",
  "createdBy": "Delaine",
  "updatedBy": "Delaine",
  "items": [
    {
      "productName": "Printer Paper",
      "quantity": 3,
      "unitPrice": 12.50
    },
    {
      "productName": "Pens",
      "quantity": 10,
      "unitPrice": 1.25
    }
  ]
}
```

### Get All Orders

```http
GET /orders
```

Optional query parameters:

```txt
page=0
size=10
sortBy=createdAt
sortDir=desc
search=acme
```

Example:

```http
GET /orders?page=0&size=10&sortBy=createdAt&sortDir=desc&search=acme
```

### Get One Order

```http
GET /orders/{id}
```

### Update an Order

```http
PUT /orders/{id}
Content-Type: application/json
```

```json
{
  "vendor": "Acme Supplies",
  "description": "Updated office supplies order",
  "status": "APPROVED",
  "updatedBy": "Delaine",
  "items": [
    {
      "productName": "Printer Paper",
      "quantity": 5,
      "unitPrice": 12.50
    }
  ]
}
```

### Patch an Order

```http
PATCH /orders/{id}
Content-Type: application/json
```

```json
{
  "status": "APPROVED",
  "updatedBy": "Delaine"
}
```

### Delete an Order

```http
DELETE /orders/{id}
```

## Order Items API

### Add an Item to an Order

```http
POST /orders/{orderId}/items
Content-Type: application/json
```

```json
{
  "productName": "Stapler",
  "quantity": 1,
  "unitPrice": 8.99
}
```

### Patch an Order Item

```http
PATCH /orders/{orderId}/items/{itemId}
Content-Type: application/json
```

```json
{
  "quantity": 2
}
```

### Delete an Order Item

```http
DELETE /orders/{orderId}/items/{itemId}
```

## Validation Rules

Order fields:

- `vendor` is required.
- `status` is required.
- `description`, `createdBy`, and `updatedBy` are optional.

Order item fields:

- `productName` is required when creating an item.
- `quantity` must be zero or greater.
- `unitPrice` must be zero or greater.

## Security

The backend currently permits public access to:

```txt
/hello
/orders
/orders/**
```

Other routes require authentication through Spring Security.

## Possible next additions:

- Authentication and role-based access
- Order status enum
- Global error response format
- Database migrations with Flyway or Liquibase
- API documentation with Swagger/OpenAPI
- Tests for service and controller behavior
