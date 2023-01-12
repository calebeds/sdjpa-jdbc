package com.calebehttps.start.spring.io.sdjpajdbc.repositories;

import com.calebehttps.start.spring.io.sdjpajdbc.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
