package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.enums.Genre;
import com.bookstore.mappers.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("Create a book - Success")
    void testCreateBook_Success(){
        BookDTO inputBookDTO = new BookDTO(null, "Book One", "Author A", "Desc",
                Genre.FICTION, 200, 10.0, 5, 1L);

        Book mappedBook = new Book(12L, "Book One", "Author A", "Desc",
                Genre.FICTION, 200, 10.0, 5, 1L);

        BookDTO mappedBookDTO = new BookDTO(12L, "Book One", "Author A", "Desc",
                Genre.FICTION, 200, 10.0, 5, 1L);

        when(bookMapper.toEntity(Mockito.any(BookDTO.class))).thenReturn(mappedBook);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(mappedBook);
        when(bookMapper.toDTO(Mockito.any(Book.class))).thenReturn(mappedBookDTO);

        BookDTO savedBook = bookService.createBook(inputBookDTO);

        Assertions.assertNotNull(savedBook);
        Assertions.assertEquals(12L,savedBook.getId());

        verify(bookMapper).toEntity(inputBookDTO);
        verify(bookRepository).save(mappedBook);
        verify(bookMapper).toDTO(mappedBook);

    }

    @Test
    @DisplayName("Create a book - Fail,throws an exception when repository fails to save book")
    void testCreateBook_Fail(){
        BookDTO inputBookDTO = new BookDTO(null, "A book", "Author A", "Desc",
                Genre.FICTION, 240, 10.0, 12, 4L);

        Book mappedBook = new Book(19L, "A book", "Author A", "Desc",
                Genre.FICTION, 240, 10.0, 12, 4L);

        when(bookMapper.toEntity(inputBookDTO)).thenReturn(mappedBook);
        when(bookRepository.save(mappedBook)).thenThrow(new RuntimeException("Database Error"));

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class
                ,() -> bookService.createBook(inputBookDTO));

        Assertions.assertEquals("Database Error",ex.getMessage());

        verify(bookMapper).toEntity(inputBookDTO);
        verify(bookRepository).save(mappedBook);
        verify(bookMapper, never()).toDTO(any());

    }

    @Test
    @DisplayName("Get all books - Success")
    void testGetAllBooks_Success(){
        Book book1 = new Book(14L, "Book 1", "Author A", "Desc",
                Genre.FICTION, 240, 12.90, 12, 4L);
        Book book2 = new Book(15L, "Book 2", "Author B", "Desc",
                Genre.FICTION, 120, 10.0, 220, 4L);
        Book book3 = new Book(16L, "Book 3", "Author C", "Desc",
                Genre.FICTION, 550, 29.90, 32, 4L);
        List<Book> booksList = Arrays.asList(book1,book2,book3);

        BookDTO bookDTO1 = new BookDTO(14L, "Book 1", "Author A", "Desc",
                Genre.FICTION, 240, 12.90, 12, 4L);
        BookDTO bookDTO2 = new BookDTO(15L, "Book 2", "Author B", "Desc",
                Genre.FICTION, 120, 10.0, 220, 4L);
        BookDTO bookDTO3 = new BookDTO(16L, "Book 3", "Author C", "Desc",
                Genre.FICTION, 550, 29.90, 32, 4L);

        when(bookRepository.findAll()).thenReturn(booksList);

        when(bookMapper.toDTO(book1)).thenReturn(bookDTO1);
        when(bookMapper.toDTO(book2)).thenReturn(bookDTO2);
        when(bookMapper.toDTO(book3)).thenReturn(bookDTO3);

        List<BookDTO> bookDTOList = bookService.getAllBooks();

        Assertions.assertNotNull(bookDTOList);
        Assertions.assertEquals(14L,bookDTOList.get(0).getId());
        Assertions.assertEquals(15L,bookDTOList.get(1).getId());
        Assertions.assertEquals(16L,bookDTOList.get(2).getId());
        Assertions.assertEquals(3,bookDTOList.size());

        verify(bookRepository).findAll();
        verify(bookMapper, times(3)).toDTO(any(Book.class));

    }

    @Test
    @DisplayName("Get all books - Fail")
    void testGetAllBooks_Fail(){
        List<Book> bookList = List.of();

        when(bookRepository.findAll()).thenReturn(bookList);
        List<BookDTO> bookDTOList = bookService.getAllBooks();

        Assertions.assertEquals(true, bookDTOList.isEmpty());

        verify(bookRepository).findAll();
        verify(bookMapper,never()).toDTO(any());

    }

    @Test
    @DisplayName("Get book by id - Succes")
    void testGetBookById_Found(){
        Book book2 = new Book(15L, "Book 2", "Author B", "Desc",
                Genre.FICTION, 120, 10.0, 220, 4L);

        BookDTO bookDTO2 = new BookDTO(15L, "Book 2", "Author B", "Desc",
                Genre.FICTION, 120, 10.0, 220, 4L);

        when(bookRepository.findById(15L)).thenReturn(Optional.of(book2));
        when(bookMapper.toDTO(book2)).thenReturn(bookDTO2);

        BookDTO bookReturned = bookService.getBookById(15L);

        Assertions.assertNotNull(bookReturned);
        Assertions.assertEquals(15L,bookReturned.getId());

        verify(bookRepository).findById(15L);
        verify(bookMapper).toDTO(book2);
    }

    @Test
    @DisplayName("Get book by id - Fail")
    void getBookById_NotFound(){

    }
}
