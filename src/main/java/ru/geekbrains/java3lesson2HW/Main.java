package ru.geekbrains.java3lesson2HW;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Connection connection;
    private static Statement stmt;


    public static void main(String[] args) {
//        taskOneAndTwo();
//        taskThree();
//        taskFour();
//        taskFive();
    }

    private static void taskFive() {
        try {
            connect();
            long t = System.currentTimeMillis();
            goodsByPrice(100, 150);
            System.out.println("\n \n Lead time: " + (System.currentTimeMillis() - t));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException d) {
            d.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private static void goodsByPrice(double minPrice, double maxPrice) throws SQLException{
        String sqlQuery = "SELECT id, title, cost FROM shop WHERE cost >= ? and cost <= ?;";
        PreparedStatement psSelectGoodsByPrice = connection.prepareStatement(sqlQuery);
        psSelectGoodsByPrice.setDouble(1, minPrice);
        psSelectGoodsByPrice.setDouble(2, maxPrice);
        ResultSet rs = psSelectGoodsByPrice.executeQuery();
        while (rs.next()){
            System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getDouble(3));
        }
    }

    private static void taskFour() {
        try {
            connect();
            long t = System.currentTimeMillis();
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter title of product which you want to change: ");
            String scanTitle = sc.nextLine();
            System.out.println("Enter new cost: ");
            int scanCost = sc.nextInt();
            PreparedStatement psUpdate = connection.prepareStatement("UPDATE shop SET cost = ? WHERE title = ?");
            psUpdate.setInt(1, scanCost);
            psUpdate.setString(2, scanTitle);
            psUpdate.executeUpdate();
            sc.close();
            System.out.println("\n \n Lead time: " + (System.currentTimeMillis() - t));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException d) {
            System.out.println("Error: there is no product with this name");
        } finally {
            disconnect();
        }
    }

    private static void taskOneAndTwo() {
        try {
            connect();
            long t = System.currentTimeMillis();
            dropTable();
            createTable();
            fillTable();
            System.out.println("\n \n Lead time: " + (System.currentTimeMillis() - t));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException d) {
            d.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private static void taskThree() {
        try {
            connect();
            long t = System.currentTimeMillis();
            selectCostOfTheProduct();
            System.out.println("\n \n Lead time: " + (System.currentTimeMillis() - t));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException d) {
            System.out.println("Error: there is no product with this name");
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
