package com.bookstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "publishers")
@Entity
@ToString(exclude = "books")
public class Publisher {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "publisher")
    private List<Book> books = new ArrayList<>();

}
