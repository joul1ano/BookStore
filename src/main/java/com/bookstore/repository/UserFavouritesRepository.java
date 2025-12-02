package com.bookstore.repository;

import com.bookstore.model.UserFavourite;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavouritesRepository extends JpaRepository<UserFavourite,Long> {
    List<UserFavourite> findAllByUserId(Long userId);
}
