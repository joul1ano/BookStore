package com.bookstore.controllers;

import com.bookstore.DTOs.PublisherDTO;
import com.bookstore.model.Publisher;
import com.bookstore.repository.PublisherRepository;
import com.bookstore.service.PublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService){
        this.publisherService = publisherService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PublisherDTO>> getAllPublishers(){
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisherById(@PathVariable Long id){
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }
}
