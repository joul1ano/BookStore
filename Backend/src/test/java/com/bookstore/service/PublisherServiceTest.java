package com.bookstore.service;

import com.bookstore.DTOs.PublisherDTO;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.PublisherMapper;
import com.bookstore.model.Publisher;
import com.bookstore.repository.PublisherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {
    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private PublisherMapper publisherMapper;

    @InjectMocks
    private PublisherService publisherService;

    @Test
    @DisplayName("Create a publisher - Success")
    void createPublisher_SavesAndReturnsDTO() {
        PublisherDTO dto = new PublisherDTO(null, "Test Pub");
        Publisher entity = Publisher.builder().id(1L).name("Test Pub").build();
        Publisher savedEntity = Publisher.builder().id(1L).name("Test Pub").build();
        PublisherDTO mappedDto = new PublisherDTO(1L, "Test Pub");

        when(publisherMapper.toEntity(dto)).thenReturn(entity);
        when(publisherRepository.save(entity)).thenReturn(savedEntity);
        when(publisherMapper.toDTO(savedEntity)).thenReturn(mappedDto);

        PublisherDTO result = publisherService.createPublisher(dto);

        assertEquals(1L, result.getId());
        assertEquals("Test Pub", result.getName());
    }

    @Test
    @DisplayName("Get all publishers - Success")
    void getAllPublishers_ReturnsMappedList() {
        Publisher entity = Publisher.builder().id(1L).name("Pub").build();
        PublisherDTO dto = new PublisherDTO(1L, "Pub");

        when(publisherRepository.findAll()).thenReturn(List.of(entity));
        when(publisherMapper.toDTO(entity)).thenReturn(dto);

        List<PublisherDTO> result = publisherService.getAllPublishers();

        assertEquals(1, result.size());
        assertEquals("Pub", result.get(0).getName());
    }

    @Test
    @DisplayName("Get publisher by id - Success")
    void getPublisherById_Found() {
        Publisher entity = Publisher.builder().id(1L).name("Pub").build();
        PublisherDTO dto = new PublisherDTO(1L, "Pub");

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(publisherMapper.toDTO(entity)).thenReturn(dto);

        PublisherDTO result = publisherService.getPublisherById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Get publisher by id - Fail - Not found")
    void getPublisherById_NotFound() {
        when(publisherRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> publisherService.getPublisherById(999L));
    }
}
