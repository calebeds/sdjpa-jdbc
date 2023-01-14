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
                Author author = new Author();
                author.setId(id);
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));
                return author;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(resultSet != null) {
                    resultSet.close();
                }

                if(ps != null) {
                    ps.close();
                }

                if(connection != null) {
                    connection.close();
                }
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
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));
                return author;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(resultSet != null) {
                    resultSet.close();
                }

                if(ps != null) {
                    ps.close();
                }

                if(connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
