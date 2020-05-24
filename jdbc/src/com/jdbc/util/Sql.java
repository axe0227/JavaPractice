package com.jdbc.util;

import org.testng.annotations.Test;
import com.jdbc.bean.People;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Sql {

    @Test
    public void testUpdate(){

        String sql = "insert into `people` set playerID = ? , birthYear = ?";
        update(sql, "axe", "1998");
    }


    @Test
    public void testSelect(){

        String sql = "select playerID, birthYear, birthDay from people where birthYear = ? and birthDay = ?";
        List<People> res = select(People.class, sql, "1970", "20");

        res.forEach(System.out::println);
    }

    /**
     * support delete, insert, update
     * @param sql sql query
     * @param args argument lists
     */

    public int update(String sql, Object ...args) {

        Connection conn = null;
        PreparedStatement ps = null;
        try {

            conn = Utils.getConnection();
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Utils.closeResource(conn, ps);
        }
        return 0;
    }

    /**
     *
     * @param sql sql query statement
     * @param args  args
     * @return the query result set
     */
    public  <T> List<T> select(Class<T> clazz, String sql, Object... args){

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Utils.getConnection();

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();

            ArrayList<T> list = new ArrayList<>();
            while (rs.next()){

                T t = clazz.getDeclaredConstructor().newInstance();
                for(int i = 0; i < columnCount; i++){

                    Object columnValue = rs.getObject(i + 1);

                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);

                    if (columnValue != null)
                        field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            Utils.closeResource(conn, ps, rs);
        }
        return null;
    }


}
