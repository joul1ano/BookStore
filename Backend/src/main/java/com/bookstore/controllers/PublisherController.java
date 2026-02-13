package com.bookstore.controllers;

import com.bookstore.DTOs.PublisherDTO;
import com.bookstore.model.Publisher;
import com.bookstore.repository.PublisherRepository;
import com.bookstore.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.hibernate.sql.ast.tree.expression.Summarization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
@Validated
@Tag(name = "Publishers", description = "Operations related to publishers")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService){
        this.publisherService = publisherService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Get all publishers",
            description = "Returns a list of all publishers. Access = [ADMIN]"
    )
    public ResponseEntity<List<PublisherDTO>> getAllPublishers(){
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get publisher by id", description = "Access = [ADMIN]")
    public ResponseEntity<PublisherDTO> getPublisherById(@PathVariable Long id){
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create new publisher", description = "Return the created publisher. Access = [ADMIN]")
    public ResponseEntity<PublisherDTO> createPublisher(@Valid @RequestBody PublisherDTO publisherDTO){
        return ResponseEntity.ok(publisherService.createPublisher(publisherDTO));
    }
}
