package com.bookstore.mappers;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.enums.Genre;
import com.bookstore.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.mockito.Mockito.*;

class BookMapperTest {

    private final BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @Test
    @DisplayName("Maps an Entity to DTO correctly")
    void testMapEntityToDTO() {
        Book inputBook = new Book(19L, "Book One", "Author A", "Desc",
                Genre.FICTION, 200, 10.0, 5, 1L);

        BookDTO result = bookMapper.toDTO(inputBook);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(19L, result.getId());
        Assertions.assertEquals("Book One", result.getTitle());
        Assertions.assertEquals("Author A", result.getAuthor());
        Assertions.assertEquals("Desc", result.getDescription());
        Assertions.assertEquals(Genre.FICTION, result.getGenre());
        Assertions.assertEquals(200, result.getNumberOfPages());
        Assertions.assertEquals(10.0, result.getPrice());
        Assertions.assertEquals(5, result.getAvailability());
        Assertions.assertEquals(1L, result.getPublisherId());
    }

    @Test
    @DisplayName("Maps a DTO to Entity correctly (id ignored)")
    void testMapDtoToEntity() {
        BookDTO inputDto = new BookDTO(null,                                  // No ID yet
                "Book One", "Author A", "Desc", Genre.FICTION,
                200, 10.0, 5, 1L
        );

        Book result = bookMapper.toEntity(inputDto);

        Assertions.assertNotNull(result);
        Assertions.assertNull(result.getId()); // ID should not be copied
        Assertions.assertEquals("Book One", result.getTitle());
        Assertions.assertEquals("Author A", result.getAuthor());
        Assertions.assertEquals("Desc", result.getDescription());
        Assertions.assertEquals(Genre.FICTION, result.getGenre());
        Assertions.assertEquals(200, result.getNumberOfPages());
        Assertions.assertEquals(10.0, result.getPrice());
        Assertions.assertEquals(5, result.getAvailability());
        Assertions.assertEquals(1L, result.getPublisherId());
    }

    @Test
    @DisplayName("toAdminDTO returns null when input entity is null")
    void testToDtoNullInput() {
        BookDTO result = bookMapper.toDTO(null);

        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("toEntity returns null when input DTO is null")
    void testToEntityNullInput() {
        Book result = bookMapper.toEntity(null);

        Assertions.assertNull(result);
    }



    }

