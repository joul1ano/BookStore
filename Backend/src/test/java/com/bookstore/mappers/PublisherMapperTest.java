package com.bookstore.mappers;

import com.bookstore.DTOs.PublisherDTO;
import com.bookstore.model.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PublisherMapperTest {

    private PublisherMapper publisherMapper;

    @BeforeEach
    void setUp() {
        publisherMapper = Mappers.getMapper(PublisherMapper.class);
    }

    @Test
    void toDTO_ShouldMapPublisherToDTO() {
        Publisher publisher = Publisher.builder()
                .id(1L)
                .name("Test Publisher")
                .books(new ArrayList<>())
                .build();

        PublisherDTO dto = publisherMapper.toDTO(publisher);

        assertEquals(publisher.getId(), dto.getId());
        assertEquals(publisher.getName(), dto.getName());
    }

    @Test
    void toEntity_ShouldMapDTOToPublisher() {
        PublisherDTO dto = PublisherDTO.builder()
                .id(2L)
                .name("Another Publisher")
                .build();

        Publisher publisher = publisherMapper.toEntity(dto);

        assertEquals(dto.getId(), publisher.getId());
        assertEquals(dto.getName(), publisher.getName());
    }

    @Test
    void toDTO_ShouldHandleNull() {
        PublisherDTO dto = publisherMapper.toDTO(null);
        assertEquals(null, dto);
    }

    @Test
    void toEntity_ShouldHandleNull() {
        Publisher publisher = publisherMapper.toEntity(null);
        assertEquals(null, publisher);
    }
}

