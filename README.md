# Product Info API

REST API for querying applicable product prices by brand, product and application date. Built with Java 17, Spring Boot 3.x and Hexagonal Architecture.

## Tech Stack

- Java 17
- Spring Boot 3.2.5
- H2 in-memory database
- JPA / Hibernate
- Lombok
- SpringDoc OpenAPI 2.3.0
- JUnit 5 / Mockito
- Maven 3.x

## How to Run

```bash
git clone https://github.com/your-username/product-info.git
cd product-info
mvn spring-boot:run
```

## How to Test

```bash
# Unit tests only
mvn test

# Unit + Integration + E2E
mvn verify
```

## Available URLs

| URL | Description |
|---|---|
| http://localhost:8080/api/v1/prices | REST Endpoint |
| http://localhost:8080/swagger-ui/index.html | Swagger UI |
| http://localhost:8080/api-docs | OpenAPI JSON |
| http://localhost:8080/h2-console | H2 Console |

## Endpoint

### GET /api/v1/prices

| Parameter | Type | Required | Example |
|---|---|---|---|
| applicationDate | LocalDateTime | Yes | 2020-06-14T10:00:00 |
| productId | Long | Yes | 35455 |
| brandId | Long | Yes | 1 |

**Success Response 200:**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50
}
```

**Error Response 404:**
```json
{
  "status": 404,
  "message": "No applicable price found for productId=35455, brandId=1, date=2019-01-01T00:00:00",
  "timestamp": "2026-05-09T19:00:00"
}
```

## Required Test Cases

| Test | applicationDate | Expected Price | Expected PriceList |
|---|---|---|---|
| Test 1 | 2020-06-14T10:00:00 | 35.50 | 1 |
| Test 2 | 2020-06-14T16:00:00 | 25.45 | 2 |
| Test 3 | 2020-06-14T21:00:00 | 35.50 | 1 |
| Test 4 | 2020-06-15T10:00:00 | 30.50 | 3 |
| Test 5 | 2020-06-16T21:00:00 | 38.95 | 4 |

## Bruno Collection

A Bruno collection is available at `/bruno/product-info-collection` with all 7 test cases ready to run.

1. Install Bruno from https://www.usebruno.com
2. Open Bruno and click "Open Collection"
3. Select the folder `/bruno/product-info-collection`
4. Run the application with `mvn spring-boot:run`
5. Execute the requests
