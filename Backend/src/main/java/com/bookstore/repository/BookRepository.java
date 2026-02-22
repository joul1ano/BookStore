package com.bookstore.repository;

import com.bookstore.enums.Genre;
import com.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findAllByIsDeletedFalse();
    Optional<Book> findById(Long id);
    List<Book> findByAvailabilityGreaterThanAndIsDeletedFalse(int availability);
    boolean existsByTitleAndAuthor(String title, String author);
    List<Book> findAllByGenreAndIsDeletedFalse(Genre genre);
    List<Book> findAllByGenreAndAvailabilityGreaterThanAndIsDeletedFalse(Genre genre, int availability);

    // paginated versions
    Page<Book> findAllByIsDeletedFalse(Pageable pageable);
    Page<Book> findByAvailabilityGreaterThanAndIsDeletedFalse(int availability, Pageable pageable);
    Page<Book> findAllByGenreAndIsDeletedFalse(Genre genre, Pageable pageable);
    Page<Book> findAllByGenreAndAvailabilityGreaterThanAndIsDeletedFalse(Genre genre, int availability, Pageable pageable);
}
