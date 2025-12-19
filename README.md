BookStore is an online bookstore backend application built with Java Spring Boot focused on developing RESTful APIs for typical e-commerce bookstore features such as managing books, shopping carts, orders, and users. It demonstrates core backend capabilities including CRUD operations, user authentication, and role-based access control.

## Features:

1) Full CRUD for Books, Orders, Users, and Shopping Cart
2) Secure user authentication and role-based access
3) Spring Boot backend with efficient data handling
4) Organized codebase ready for extension

## Tech Stack:

1) Language: Java
2) Framework: Spring Boot
3) Build Tool: Maven
4) Database: PostgreSQL
5) Authentication: JWT / Spring Security
6) Version Control: Git / GitHub

## Installation:
1) Clone the repository: A) git clone https://github.com/joul1ano/BookStore.git B) cd BookStore
2) Build with Maven:
   mvn clean install
3) Configure Database:
   Create and configure your database connection in application.properties or application.yml
4) Run the Application:
   mvn spring-boot:run

## API Documentation (Swagger UI)

This project uses **Swagger UI** to document and test the REST API endpoints.

After running the application, open your browser and navigate to:

http://localhost:8080/swagger-ui/index.html

From the Swagger UI you can:
- View all available endpoints grouped by controller
- See request/response models
- Test endpoints directly from the browser
- Authorize requests (if authentication is enabled)



