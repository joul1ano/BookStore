package com.bookstore.DTOs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedResponseDTO<T> {
    private List<T> content;
    private int page;         // current page
    private int size;         // items per page
    private long totalElements; // total number of items
    private int totalPages;   // total number of pages
    private boolean last;     // is this the last page?
}