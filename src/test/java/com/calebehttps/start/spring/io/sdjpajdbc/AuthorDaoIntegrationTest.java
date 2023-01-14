package com.calebehttps.start.spring.io.sdjpajdbc;

import com.calebehttps.start.spring.io.sdjpajdbc.dao.AuthorDao;
import com.calebehttps.start.spring.io.sdjpajdbc.dao.AuthorDaoImpl;
import com.calebehttps.start.spring.io.sdjpajdbc.domain.Author;
import com.calebehttps.start.spring.io.sdjpajdbc.repositories.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
@DataJpaTest
//@ComponentScan(basePackages = {"com.calebehttps.start.spring.io.sdjpajdbc.dao"})
@Import(AuthorDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorDaoIntegrationTest {

    @Autowired
    AuthorDao authorDao;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testGetAuthor() {
        Author author = authorDao.getById(1L);
        assertThat(author).isNotNull();
    }

    @Test
    void testGetAuthorByName() {
        Author author = authorDao.getAuthorByName("Craig", "Walls");
        assertThat(author).isNotNull();
    }

    @Test
    void testSaveNewAuthor() {
        Author author = new Author();
        author.setFirstName("Calebe");
        author.setLastName("Oliveira");
        Author saved = authorDao.saveNewAuthor(author);

        assertThat(saved).isNotNull();
    }

    @Test
    void testUpdateAuthor() {
        Author author = new Author();
        author.setFirstName("Calebe");
        author.setLastName("O");

        Author saved = authorDao.saveNewAuthor(author);

        saved.setLastName("Oliveira");
        Author updated = authorDao.updateAuthor(saved);

        assertThat(updated.getLastName()).isEqualTo("Oliveira");
    }

    @Test
    void testDeleteAuthor() {
        Author author = new Author();
        author.setFirstName("Calebe");
        author.setLastName("O");

        Author saved = authorDao.saveNewAuthor(author);

        authorDao.deleteAuthorById(saved.getId());

        Author deleted = authorDao.getById(saved.getId());

        assertThat(deleted).isNull();
    }
}
