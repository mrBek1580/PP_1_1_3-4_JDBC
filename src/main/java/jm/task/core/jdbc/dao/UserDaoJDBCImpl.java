package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();

    public void createUsersTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY  ,
                    firstName VARCHAR(64) NOT NULL ,
                    lastName VARCHAR(64) NOT NULL ,
                    age INT NOT NULL )
                    """;
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute(sql);
            connection.commit();
            System.out.println("Таблица создана");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void dropUsersTable() {
        String sql = """
                    DROP TABLE IF EXISTS users;
                    """;
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute(sql);
            connection.commit();
            System.out.println("Таблица удалена");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = """
                INSERT INTO users (firstName, lastName, age) VALUES (?,?,?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Пользователь добавлен");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void removeUserById(long id) {
        String sql = """
                DELETE FROM users WHERE id = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Пользователь удален");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        String sql = """
                SELECT id, firstName, lastName, age FROM users
                """;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultset = statement.executeQuery(sql);
            while (resultset.next()) {
                connection.setAutoCommit(false);
                User user = new User();
                user.setId(resultset.getLong("id"));
                user.setName(resultset.getString("firstName"));
                user.setName(resultset.getString("lastName"));
                user.setAge(resultset.getByte("age"));
                allUsers.add(user);
                connection.commit();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return allUsers;
    }

    public void cleanUsersTable() {
        String sql = """
                TRUNCATE TABLE users
                """;
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute(sql);
            connection.commit();
            System.out.println("Таблица очищена");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
