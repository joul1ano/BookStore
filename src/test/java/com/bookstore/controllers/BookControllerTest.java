package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.enums.Genre;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {
    @MockitoBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /books/{id} returns the bookDTO with the provided id")
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
    @DisplayName("GET /books/{id} returns 404 when book is not found")
    void testGetBookByIdNotFound() throws Exception{
        ResourceNotFoundException exception = new ResourceNotFoundException("Book with id: 9 not found");
        doThrow(exception).when(bookService).getBookById(9L);

        mockMvc.perform(get("/books/{id}",9))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book with id: 9 not found"));

    }

    @Test
    @DisplayName("POST /books returns the bookDTO including the id when the book is created succesfully")
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
    //TODO WRITE THE TEST FOR UPDATE

    @Test
    @DisplayName("DELETE /books/{id} returns 204 no content when a book is deleted succesfully")
    void testDeleteBookByIdSuccess() throws Exception{
        doNothing().when(bookService).deleteBookById(5L);
        mockMvc.perform(delete("/books/{id}", 5))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /books/{id} returns 404 not found when trying to delete a book with wrong id")
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
