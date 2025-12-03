package com.bookstore.service;

import com.bookstore.DTOs.PublisherDTO;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.PublisherMapper;
import com.bookstore.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public PublisherService(PublisherRepository publisherRepository, PublisherMapper publisherMapper){
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    public List<PublisherDTO> getAllPublishers(){
        return publisherRepository.findAll().stream().map(publisherMapper::toDTO).toList();
    }

    public PublisherDTO getPublisherById(Long id){
        return publisherRepository.findById(id).map(publisherMapper::toDTO)
                .orElseThrow(()-> new ResourceNotFoundException("Publisher with id: " + id +" not found"));
    }
}
