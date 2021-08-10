package com.ohapon.eshop.db;

import com.ohapon.eshop.PropertiesLoader;
import com.ohapon.eshop.dao.jdbc.ConnectionFactory;

import java.io.*;
import java.sql.*;

public class DBInitializer {

    private ConnectionFactory connectionFactory;

    public DBInitializer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void init() {
        try {
            Connection con = getConnection();
            createDB(con);
            populateDB(con);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createDB(Connection con) throws SQLException {
        String sql = loadSQL("dbscripts/create_db.sql");
        Statement stm = con.createStatement();
        stm.execute(sql);
        stm.close();
    }

    private void populateDB(Connection con) throws SQLException {
        String sql = loadSQL("dbscripts/populate_db.sql");
        Statement stm = con.createStatement();
        stm.execute(sql);
    }

    private String loadSQL(String path) {
            try (InputStream is = PropertiesLoader.class.getClassLoader().getResourceAsStream(path)) {
                return loadSQL(is);
            } catch (IOException e) {
                throw new RuntimeException("Can't load SQL script from file: " + path, e);
            }
    }

    private String loadSQL(InputStream is) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(is))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }
        }
        return builder.toString();
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        return connectionFactory.getConnection();
    }

}
