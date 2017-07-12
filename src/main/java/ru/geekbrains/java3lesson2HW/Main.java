package ru.geekbrains.java3lesson2HW;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Connection connection;
    private static Statement stmt;


    public static void main(String[] args) {
        try {
            connect();
            long t = System.currentTimeMillis();
//            taskOne();
            selectCostOfTheProduct();
            System.out.println(System.currentTimeMillis() - t);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }


    }

    private static void selectCostOfTheProduct() throws SQLException {
        Scanner sc = new Scanner(System.in);
        String scanString = sc.nextLine();
        PreparedStatement psCost = connection.prepareStatement("SELECT cost FROM shop WHERE title = ?;");
        psCost.setString(1, scanString);
        ResultSet rs = psCost.executeQuery();
        System.out.println("The cost of the product = " + rs.getString(1));
        sc.close();
    }

    private static void badSelect(String sqlQuery) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT cost FROM shop WHERE title = 'Product 9986';");
        System.out.println("The cost of the product = " + rs.getInt(1));
    }

    private static void taskOne() throws SQLException {
        dropTable();
        createTable();
        fillTable();
    }

    private static void fillTable() throws SQLException {
        connection.setAutoCommit(false);
        int price = 10;
        for (int i = 1; i <= 10000; i++) {
            stmt.executeUpdate("INSERT INTO shop(prodid, title, cost) VALUES (" + i + ", 'Product " + i + "', '" + (price * i) + "')");
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
