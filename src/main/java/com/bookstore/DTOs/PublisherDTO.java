package com.bookstore.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherDTO {
    @Positive(message = "Id must be a postitive number")
    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
}
