package com.bookstore.repository;

import com.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    List<Book> findByAvailabilityGreaterThan(int availability);
    boolean existsByTitleAndAuthor(String title, String author);
}
