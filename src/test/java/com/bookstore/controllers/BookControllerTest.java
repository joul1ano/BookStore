package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.enums.Genre;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @MockitoBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Returns the BookDTO with the requested ID when the book exists")
    void testGetBookByIdFound() throws Exception{
        BookDTO mockBook = new BookDTO(3L,"Java","John Doe","Learning Java",
                Genre.HORROR,362,39.80,95,212L);
        doReturn(mockBook).when(bookService).getBookById(3L);

        mockMvc.perform(get("/books/{id}",3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id",is(3)))
                .andExpect(jsonPath("$.title",is("Java")))
                .andExpect(jsonPath("$.author",is("John Doe")))
                .andExpect(jsonPath("$.description",is("Learning Java")))
                .andExpect(jsonPath("$.genre",is("HORROR")))
                .andExpect(jsonPath("$.numberOfPages",is(362)))
                .andExpect(jsonPath("$.price",is(39.80)))
                .andExpect(jsonPath("$.availability",is(95)))
                .andExpect(jsonPath("$.publisherId",is(212)));

    }

    @Test
    @DisplayName("Returns 404 Not Found when a book with the given ID does not exist")
    void testGetBookByIdNotFound() throws Exception{
        ResourceNotFoundException exception = new ResourceNotFoundException("Book with id: 9 not found");
        doThrow(exception).when(bookService).getBookById(9L);

        mockMvc.perform(get("/books/{id}",9))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book with id: 9 not found"));

    }

    @Test
    @DisplayName("Returns 400 bad request when an ID is given in the wrong format")
    void testGetBookById_withWrongFormat() throws Exception{

        mockMvc.perform(get("/books/{id}","abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Invalid type for parameter 'id'. Expected a positive number but received: abc"));

    }

    @Test
    @DisplayName("Returns a BookDTO with an assigned ID when a new book is successfully created")
    void testCreateBookSuccess() throws Exception{
        BookDTO mockBookToBeCreated = new BookDTO(null,"Python","John Doe","Learning Python",
                Genre.HORROR,282,29.80,105,212L);

        BookDTO mockBookToBeReturned = new BookDTO(7L,"Python","John Doe","Learning Python",
                Genre.HORROR,282,29.80,105,212L);

        doReturn(mockBookToBeReturned).when(bookService).createBook(mockBookToBeCreated);

        ObjectMapper objMapper = new ObjectMapper();
        String bookInJson = objMapper.writeValueAsString(mockBookToBeCreated);
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(bookInJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.title").value("Python"))
                .andExpect(jsonPath("$.author").value("John Doe"))
                .andExpect(jsonPath("$.description").value("Learning Python"))
                .andExpect(jsonPath("$.genre").value("HORROR"))
                .andExpect(jsonPath("$.numberOfPages").value(282))
                .andExpect(jsonPath("$.price").value(29.80))
                .andExpect(jsonPath("$.availability").value(105))
                .andExpect(jsonPath("$.publisherId").value(212));
    }

    @Test
    @DisplayName("Returns 400 Bad Request with validation error messages when trying to create a book with invalid book data")
    void testCreateBookFail() throws Exception{
        BookDTO mockBookToBeCreated = new BookDTO(null,"","","A journey",
                Genre.HORROR,0,-2.0,-10,2L);

        ObjectMapper objMapper = new ObjectMapper();
        String bookInJson = objMapper.writeValueAsString(mockBookToBeCreated);

        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                .content(bookInJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.title").value("Title cannot be blank"))
                .andExpect(jsonPath("$.message.author").value("Author cannot be blank"))
                .andExpect(jsonPath("$.message.numberOfPages").value("Number of pages must be a positive number"))
                .andExpect(jsonPath("$.message.price").value("Price must be a positive number"))
                .andExpect(jsonPath("$.message.availability").value("Availability cannot be negative"));


    }

    @Test
    @DisplayName("Returns 204 No Content when a book is deleted successfully")
    void testDeleteBookByIdSuccess() throws Exception{
        doNothing().when(bookService).deleteBookById(5L);
        mockMvc.perform(delete("/books/{id}", 5))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Returns 404 Not Found when attempting to delete a non-existent book")
    void testDeleteBookByIdFail() throws Exception{
        ResourceNotFoundException exception = new ResourceNotFoundException("Book with id: 5 not found");
        doThrow(exception).when(bookService).deleteBookById(5L);

        mockMvc.perform(delete("/books/{id}", 5))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book with id: 5 not found"));
    }
    //TODO WRITE DELETE TEST FOR CASE THAT YOU DELETE A BOOK THAT IS REFERENCED BY AN ACTIVE ORDER
}
