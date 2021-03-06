package com.codecool.dungeoncrawl.logic.model;
import javax.sql.DataSource;
import java.sql.*;

public class Saves {


    DataSource dataSource;

    public Saves(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addSave(Date date, String map) {
        try(Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO saves (map) VALUES (?)";
            PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, map);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
        } catch (SQLException throwables) {
            throw new RuntimeException("Error while adding new Author.", throwables);
        }
    }

    public String getLastSave() {
        try(Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM saves ORDER BY id DESC LIMIT 1";
            PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getString(2);
        } catch (SQLException throwables) {
            throw new RuntimeException("Error while adding new Author.", throwables);
        }
    }
}
