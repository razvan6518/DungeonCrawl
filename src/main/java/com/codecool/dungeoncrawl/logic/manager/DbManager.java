package com.codecool.dungeoncrawl.logic.manager;
import com.codecool.dungeoncrawl.logic.model.Saves;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;


public class DbManager {

    public Saves saves;

    public static DataSource connect() throws SQLException {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(5432);
        ds.setDatabaseName("dungeon");
        ds.setUser("razvan");
        ds.setPassword("123456");
        return ds;
    }

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        saves = new Saves(dataSource);
    }
}
