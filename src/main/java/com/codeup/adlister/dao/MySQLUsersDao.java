package com.codeup.adlister.dao;

import com.codeup.adlister.models.User;

import java.sql.*;

import com.mysql.cj.jdbc.Driver;


public class MySQLUsersDao implements Users {

    private Connection connection;

    public MySQLUsersDao(Config config) {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUser(),
                    config.getPassword()
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public User findByUsername(String username) {
        String query = "SELECT * FROM users where username = ? LIMIT 1";
        try{
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,username);
            ResultSet rs= statement.executeQuery();
//            return extractUser(statement.executeQuery());
            //if user doesnt exist, make a new user
            //if there is a list of rows back, loop the rs. If not you do not need the loop
            if(!rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        }catch(SQLException ex){
            throw new RuntimeException("Error finding user by username",ex);
        }
        return null;
    }

    @Override
    public Long insert(User user) {
        String query = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
        try {
            //return generated keys - i want the keys to come back once this runs
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating new user", e);
        }
    }

//    private User extractUser(ResultSet rs) throws SQLException {
//        if (! rs.next()) {
//            return null;
//        }
//        return new User(
//                rs.getLong("id"),
//                rs.getString("username"),
//                rs.getString("email"),
//                rs.getString("password")
//        );
//    }
}
