BookStore is a full-stack web application featuring a Java Spring Boot backend and a React frontend built with Vite and Bootstrap. The system provides a complete solution for managing an online bookstore through a RESTful API and a responsive user interface, supporting role-based access for both customers and administrators.

## Features:

1) User Perspective <br />
   Customers can browse the book collection, add items to a list of favorites, and manage a shopping cart. The system allows users to place orders and track their order history and status.
2) Admin Perspective <br />
   Administrators have access to tools to browse the full list of books and registered users. They can perform CRUD operations to create new book entries or update existing ones, as well as manage customer orders and update their fulfillment status.

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



