package com.bookstore.controllers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.enums.Genre;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @DisplayName("GET /books/3 - Found")
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
    @DisplayName("GET /books/9 - Not Found")
    void testGetBookByIdNotFound() throws Exception{
        ResourceNotFoundException exception = new ResourceNotFoundException("Book with id: 9 not found");
        doThrow(exception).when(bookService).getBookById(9L);

        mockMvc.perform(get("/books/{id}",9))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book with id: 9 not found"));

    }
}
