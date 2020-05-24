package com.jdbc.connection;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {

    @Test
    public void testConnection() {

        MysqlDataSource ds = null;
        FileInputStream fis = null;

        try {

            fis = new FileInputStream("jdbc.properties");

            Properties info = new Properties();
            info.load(fis);
            ds = new MysqlDataSource();

            ds.setUser(info.getProperty("user"));
            ds.setPassword(info.getProperty("password"));
            ds.setURL(info.getProperty("url"));
            ds.setServerTimezone("UTC");

            Connection conn = ds.getConnection();
            System.out.println(conn != null);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {

        ConnectionTest conn = new ConnectionTest();

    }

}
