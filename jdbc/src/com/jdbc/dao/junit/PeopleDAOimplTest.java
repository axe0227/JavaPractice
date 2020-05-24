package com.jdbc.dao.junit;
import com.jdbc.bean.People;
import com.jdbc.dao.PeopleDAOimpl;
import com.jdbc.util.Utils;
import  org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.List;

class PeopleDAOimplTest {

    PeopleDAOimpl dao = new PeopleDAOimpl();

    @Test
    void insert() {
        Connection conn = null;
        try {
            conn = Utils.getConnection();
            People p = new People("axelord", 1998, 27);
            dao.insert(conn, p);
            System.out.println("insert succeeds");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeResource(conn, null);
        }
    }

    @Test
    void deleteById() {
    }

    @Test
    void update() {
    }

    @Test
    void getPeopleById() {
    }

    @Test
    void getAll() {
        Connection conn = null;
        try {
            conn = Utils.getConnection();
            List<People> list =  dao.getAll(conn);
            list.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeResource(conn, null);
        }
    }

    @Test
    void getCount() {
    }
}