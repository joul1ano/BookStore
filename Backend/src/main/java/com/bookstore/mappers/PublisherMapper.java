package com.bookstore.mappers;

import com.bookstore.DTOs.PublisherDTO;
import com.bookstore.model.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
    PublisherDTO toDTO(Publisher publisher);
    Publisher toEntity(PublisherDTO publisherDTO);
}
