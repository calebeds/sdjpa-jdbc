package com.calebehttps.start.spring.io.sdjpajdbc.dao;

import com.calebehttps.start.spring.io.sdjpajdbc.domain.Author;

public interface AuthorDao {
    Author getById(Long id);
    Author getAuthorByName(String firstName, String lastName);
    Author saveNewAuthor(Author author);

    Author updateAuthor(Author author);
    void deleteAuthorById(Long id);
}
