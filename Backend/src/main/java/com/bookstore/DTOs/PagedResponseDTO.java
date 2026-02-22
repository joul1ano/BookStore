package com.bookstore.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponseDTO<T> {
    private List<T> content;
    private int page;         // current page
    private int size;         // items per page
    private long totalElements; // total number of items
    private int totalPages;   // total number of pages
    private boolean last;     // is this the last page?
}