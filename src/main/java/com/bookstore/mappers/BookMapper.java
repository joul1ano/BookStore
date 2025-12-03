package com.bookstore.mappers;

import com.bookstore.model.Book;
import com.bookstore.DTOs.BookDTO;
import com.bookstore.model.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(source = "publisher.id", target = "publisherId")
    BookDTO toDTO(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publisher", source = "publisherId")
    Book toEntity(BookDTO bookDTO);

    default Publisher mapPublisherIdToPublisher(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Publisher ID cannot be null");
        }
        Publisher publisher = new Publisher();
        publisher.setId(id);
        return publisher;
    }

}
