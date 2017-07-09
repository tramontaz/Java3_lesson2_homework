package ru.geekbrains.java3lesson2HW;

import java.sql.*;

public class Main {
    private static Connection connection;
    private static Statement stmt;


    public static void main(String[] args) {
        try {
            connect();
            long t = System.currentTimeMillis();
            dropTable();
            createTable();
            fillTable();
            System.out.println(System.currentTimeMillis() - t);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

    }

    private static void fillTable() throws SQLException {
        connection.setAutoCommit(false);
        int price = 10;
        for (int i = 0; i < 10000; i++) {
            stmt.executeUpdate("INSERT INTO shop(prodid, title, cost) VALUES (" + i + ", 'Product " + i + "', '" + (price + 10) + "')");
        }
        connection.commit();
        connection.setAutoCommit(true);
    }

    private static void dropTable() throws SQLException {
        stmt.execute("DROP TABLE IF EXISTS shop");
    }

    private static void deleteAllFromTable() throws SQLException {
        String sqlQuery = "DELETE FROM shop";
        stmt.execute(sqlQuery);
    }

    private static void createTable() throws SQLException {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS shop (\n" +
                "    id     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    prodid INTEGER NOT NULL,\n" +
                "    title  STRING  NOT NULL,\n" +
                "    cost   DOUBLE  NOT NULL\n" +
                ");";
        stmt.execute(sqlQuery);
    }

    private static void disconnect() {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:dbJ3L2HW.db");
        stmt = connection.createStatement();
    }
}
