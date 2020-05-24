package com.jdbc.util;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Utils {

    /**
     * create a jdbc connection
     * @return a new connection
     */
    public static Connection getConnection(){

        MysqlDataSource ds;
        FileInputStream fis = null;
        Connection conn = null;

        try {

            fis = new FileInputStream("jdbc.properties");

            Properties info = new Properties();
            info.load(fis);

            ds = new MysqlDataSource();

            ds.setUser(info.getProperty("user"));
            ds.setPassword(info.getProperty("password"));
            ds.setURL(info.getProperty("url"));
            ds.setServerTimezone("UTC");

            conn = ds.getConnection();

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

        return  conn;
    }


    /**
     * close resource
     * @param conn the connection to close
     * @param ps PrepareStatement
     * @return void
     */

    public static void closeResource(Connection conn, Statement ps){

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null){
            try {
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     *  close resource
     * @param conn Connection
     * @param ps PrepareStatement
     * @param rs ResultSet
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {


        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null){
            try {
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }

        if (ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
