package com.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;
import org.mapstruct.control.MappingControl;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "user_favourites", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"}))
@Entity
public class UserFavourite {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id",
            foreignKey = @ForeignKey(name = "book_id",
                    foreignKeyDefinition = "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE"))
    private Book book;

    @Column(name = "added_at")
    private LocalDateTime addedAt;
}
