package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.enums.Genre;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.model.Publisher;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.PublisherRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
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

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("Create a book - Success")
    void testCreateBook_Success(){
        BookDTO inputBookDTO = new BookDTO(null, "Book One", "Author A", "Desc",
                Genre.FICTION, 200, 10.0, 5, 1L);

        Publisher publisher = Publisher.builder().id(1L).name("Ianos").build();
        Book mappedBook = new Book(12L, "Book One", "Author A", "Desc",
                Genre.FICTION, 200, 10.0, 5, publisher);

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
    @DisplayName("Create a book - Fail (throws an exception when repository fails to save book)")
    void testCreateBook_Fail(){
        BookDTO inputBookDTO = new BookDTO(null, "A book", "Author A", "Desc",
                Genre.FICTION, 240, 10.0, 12, 4L);

        Publisher publisher = Publisher.builder().id(4L).name("Meltemi").build();
        Book mappedBook = new Book(19L, "A book", "Author A", "Desc",
                Genre.FICTION, 240, 10.0, 12, publisher);

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
        Publisher publisher = Publisher.builder().id(4L).name("Meltelmi").build();
        Book book1 = new Book(14L, "Book 1", "Author A", "Desc",
                Genre.FICTION, 240, 12.90, 12, publisher);
        Book book2 = new Book(15L, "Book 2", "Author B", "Desc",
                Genre.FICTION, 120, 10.0, 220, publisher);
        Book book3 = new Book(16L, "Book 3", "Author C", "Desc",
                Genre.FICTION, 550, 29.90, 32, publisher);
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
    @DisplayName("Get all books - Fail (No existing books in catalogue)")
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
        Publisher publisher = Publisher.builder().id(4L).name("Meltemi").build();
        Book book2 = new Book(15L, "Book 2", "Author B", "Desc",
                Genre.FICTION, 120, 10.0, 220, publisher);

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
    @DisplayName("Get book by id - Fail (Book doesn't exist)")
    void getBookById_NotFound(){
        when(bookRepository.findById(25L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> bookService.getBookById(25L));

        Assertions.assertEquals("Book with id: 25 not found",ex.getMessage());

        verify(bookRepository).findById(25L);
    }

    @Test
    @DisplayName("Update book by id - Success")
    void testUpdateBookById_Success(){
        Publisher publisher = Publisher.builder().id(4L).name("Meltemi").build();
        Book bookToBeUpdated = new Book(14L, "Book 1", "Author A", "Desc",
                Genre.FICTION, 240, 12.90, 12, publisher);

        BookDTO inputBookDTO = new BookDTO(14L, "Book 1", "Author A", "Desc",
                Genre.FICTION, 240, 17.90, 24, 4L);
        Book inputBook = new Book(14L, "Book 1", "Author A", "Desc",
                Genre.FICTION, 240, 17.90, 24, publisher);

        when(bookRepository.findById(14L)).thenReturn(Optional.of(bookToBeUpdated));
        when(bookRepository.save(bookToBeUpdated)).thenReturn(inputBook);
        when(bookMapper.toDTO(inputBook)).thenReturn(inputBookDTO);
        when(publisherRepository.findById(4L)).thenReturn(Optional.of(publisher));

        BookDTO updatedBookDTO = bookService.updateBookById(14L,inputBookDTO);

        Assertions.assertNotNull(updatedBookDTO);
        Assertions.assertEquals(14L,updatedBookDTO.getId());
        Assertions.assertEquals(17.90,updatedBookDTO.getPrice());
        Assertions.assertEquals(24,updatedBookDTO.getAvailability());

        verify(bookRepository).findById(14L);
        verify(bookRepository).save(inputBook);
        verify(bookMapper).toDTO(inputBook);
    }

    @Test
    @DisplayName("Update book by id - Fail (Book doesn't exist)")
    void testUpdateBookById_Fail(){
        BookDTO bookDTO = new BookDTO();
        when(bookRepository.findById(25L)).thenReturn(Optional.empty());


        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> bookService.updateBookById(25L,bookDTO));

        Assertions.assertEquals("Book with id: 25 not found",ex.getMessage());

        verify(bookRepository).findById(25L);
        verify(bookRepository,never()).save(any());
    }

    @Test
    @DisplayName("Delete book by id - Success")
    void testDeleteBookById_Success(){
        Publisher publisher = Publisher.builder().id(4L).name("Meltemi").build();
        Book book = new Book(14L, "Book 1", "Author A", "Desc",
                Genre.FICTION, 240, 17.90, 24, publisher);
        when(bookRepository.findById(14L)).thenReturn(Optional.of(book));

        Assertions.assertDoesNotThrow(()->bookService.deleteBookById(14L));

        verify(bookRepository).findById(14L);
        verify(bookRepository).delete(book);
    }

    @Test
    @DisplayName("Delete book by id - Fail (Book doesn't exist")
    void testDeleteBookById_Fail(){
        when(bookRepository.findById(25L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,()->bookService.deleteBookById(25L));

        verify(bookRepository).findById(25L);
        verify(bookRepository,never()).deleteById(any());
    }
}
