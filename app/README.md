# Library System (JDBC, Spring Boot)

## Project Structure

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── library
│   │   │           ├── controller
│   │   │           │   └── BookController.java
│   │   │           ├── dao
│   │   │           │   ├── BookDao.java
│   │   │           │   └── impl
│   │   │           │       └── BookDaoImpl.java
│   │   │           ├── exception
│   │   │           │   ├── GlobalExceptionHandler.java
│   │   │           │   └── ResourceNotFoundException.java
│   │   │           ├── model
│   │   │           │   └── Book.java
│   │   │           ├── security
│   │   │           │   ├── InMemoryUserDetailsService.java
│   │   │           │   └── SecurityConfig.java
│   │   │           ├── service
│   │   │           │   ├── BookService.java
│   │   │           │   └── impl
│   │   │           │       └── BookServiceImpl.java
│   │   │           └── LibraryApplication.java
│   │   └── resources
│   │       ├── application.properties
│   │       ├── schema.sql
│   │       └── data.sql
├── pom.xml
└── .gitignore
```

## Installation

1. **Clone the repository**
   ```
   git clone <repo-url>
   cd library-system
   ```
2. **Build the project**
   ```
   mvn clean install
   ```
3. **Run the application**
   ```
   mvn spring-boot:run
   ```

## Usage

- Access the API at `http://localhost:8080/api/books`
- H2 Console at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:librarydb`)
- Default users:
  - user/user (ROLE_USER)
  - admin/admin (ROLE_ADMIN)

## Endpoints

- `GET /api/books` - List all books
- `GET /api/books/{id}` - Get book by ID
- `POST /api/books` - Create book (ADMIN only)
- `PUT /api/books/{id}` - Update book (ADMIN only)
- `DELETE /api/books/{id}` - Delete book (ADMIN only)

## Notes
- Uses Spring Security with in-memory users
- Uses JDBC (no JPA)
- SQL scripts in `src/main/resources`
