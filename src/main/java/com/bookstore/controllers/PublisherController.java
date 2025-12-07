package com.bookstore.controllers;

import com.bookstore.DTOs.PublisherDTO;
import com.bookstore.model.Publisher;
import com.bookstore.repository.PublisherRepository;
import com.bookstore.service.PublisherService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
@Validated
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PublisherDTO> createPublisher(@Valid @RequestBody PublisherDTO publisherDTO){
        return ResponseEntity.ok(publisherService.createPublisher(publisherDTO));
    }
}
