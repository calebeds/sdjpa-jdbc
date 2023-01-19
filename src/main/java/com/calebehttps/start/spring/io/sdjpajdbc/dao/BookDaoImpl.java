package com.calebehttps.start.spring.io.sdjpajdbc.dao;

import com.calebehttps.start.spring.io.sdjpajdbc.domain.Book;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class BookDaoImpl implements BookDao {

    private final DataSource source;

    public BookDaoImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public Book findBookById(Long id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("SELECT * FROM book WHERE id = ?");
            ps.setLong(1, id);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                return getBookByRS(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                closeAll(resultSet, ps, connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }



    @Override
    public Book findBookByTitle(String title) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("SELECT * FROM book WHERE title = ?");
            ps.setString(1, title);
            resultSet = ps.executeQuery();

            if(resultSet.next()) {
                return getBookByRS(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                closeAll(resultSet, ps, connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }



    @Override
    public Book saveNewBook(Book book) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("INSERT INTO book (title, isbn, publisher, author_id) VALUES(?, ?, ?, ?)");
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setString(3, book.getPublisher());
            ps.setLong(4, book.getAuthorId());
            ps.execute();

            Statement statement = connection.createStatement();
            resultSet = ps.executeQuery("SELECT LAST_INSERT_ID()");

            if(resultSet.next()) {
               Long savedId = resultSet.getLong(1);
               return this.findBookById(savedId);
            }

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                closeAll(resultSet, ps, connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    @Override
    public Book updateBook(Book book) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("UPDATE book SET title = ?, isbn = ?, publisher = ?, author_id =? WHERE id =?");
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setString(3, book.getPublisher());
            ps.setLong(4, book.getAuthorId());
            ps.setLong(5, book.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                closeAll(resultSet, ps, connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return findBookById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("DELETE FROM book WHERE id = ?");
            ps.setLong(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                closeAll(null, ps, connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Book getBookByRS(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setTitle(resultSet.getString("title"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setAuthorId(resultSet.getLong("author_id"));
        return book;
    }

    private void closeAll(ResultSet resultSet, PreparedStatement ps, Connection connection) throws SQLException {
        if(resultSet != null) {
            resultSet.close();
        }

        if(ps != null) {
            ps.close();
        }

        if(connection != null) {
            connection.close();
        }
    }
}
