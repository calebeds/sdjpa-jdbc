package com.calebehttps.start.spring.io.sdjpajdbc.repositories;

import com.calebehttps.start.spring.io.sdjpajdbc.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
