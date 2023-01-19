package com.calebehttps.start.spring.io.sdjpajdbc;

import com.calebehttps.start.spring.io.sdjpajdbc.dao.BookDao;
import com.calebehttps.start.spring.io.sdjpajdbc.dao.BookDaoImpl;
import com.calebehttps.start.spring.io.sdjpajdbc.domain.Book;
import com.calebehttps.start.spring.io.sdjpajdbc.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@Import(BookDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookDaoIntegrationTest {
    @Autowired
    BookDao bookDao;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testFindBookById() {
        Book book = bookDao.findBookById(1L);
        assertThat(book).isNotNull();
    }

    @Test
    void testFindBookByTitle() {
        Book book = bookDao.findBookByTitle("Spring Boot in Action, 1st Edition");
        assertThat(book).isNotNull();
    }

    @Test
    void testSaveNewBook() {
        Book book = new Book();
        book.setTitle("An Interesting Title");
        book.setIsbn("ISBN");
        book.setAuthorId(1L);
        book.setPublisher("Uncanny Publisher");
        Book saved = bookDao.saveNewBook(book);
        assertThat(saved).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void testUpdateBook() {
        Book book = new Book();
        book.setTitle("An Interesting Title");
        book.setIsbn("ISBN");
        book.setAuthorId(1L);
        book.setPublisher("Uncanny Publisher");
        Book saved = bookDao.saveNewBook(book);

        saved.setPublisher("Unknown");

        Book updated = bookDao.updateBook(saved);
        assertThat(updated).isNotNull();
        assertThat(updated.getPublisher()).isEqualTo("Unknown");
    }

    @Test
    void testDeleteBookById() {
        Book book = new Book();
        book.setTitle("An Interesting Title");
        book.setIsbn("ISBN");
        book.setAuthorId(1L);
        book.setPublisher("Uncanny Publisher");
        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        Book deleted = bookDao.findBookById(saved.getId());
        assertThat(deleted).isNull();
    }
}
