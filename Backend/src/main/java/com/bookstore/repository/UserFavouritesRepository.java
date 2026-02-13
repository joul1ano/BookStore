package com.bookstore.repository;

import com.bookstore.model.UserFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavouritesRepository extends JpaRepository<UserFavourite,Long> {
    List<UserFavourite> findAllByUser_Id(Long userId);
    Optional<UserFavourite> findByUser_IdAndBook_Id(Long userId, Long bookId);
}
