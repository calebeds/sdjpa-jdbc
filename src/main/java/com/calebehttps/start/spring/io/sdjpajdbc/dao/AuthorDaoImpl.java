package com.calebehttps.start.spring.io.sdjpajdbc.dao;

import com.calebehttps.start.spring.io.sdjpajdbc.domain.Author;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class AuthorDaoImpl implements AuthorDao {
    private final DataSource source;

    public AuthorDaoImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public Author getById(Long id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("SELECT * FROM author WHERE id = ?");
            ps.setLong(1, id);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
             return getAuthorByRS(resultSet);
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
    public Author getAuthorByName(String firstName, String lastName) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("SELECT * FROM author WHERE first_name = ? AND last_name = ?");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                return getAuthorByRS(resultSet);
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
    public Author saveNewAuthor(Author author) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUES (?, ?)");
            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.execute();

            Statement statement = connection.createStatement();
            resultSet = ps.executeQuery("SELECT LAST_INSERT_ID()");
            if(resultSet.next()) {
                Long savedId = resultSet.getLong(1);
                return this.getById(savedId);
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
    public Author updateAuthor(Author author) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?");
            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.setLong(3, author.getId());
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

        return getById(author.getId());
    }

    private Author getAuthorByRS(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));
        return author;
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
