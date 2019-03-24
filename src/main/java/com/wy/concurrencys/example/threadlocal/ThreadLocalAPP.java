package com.wy.concurrencys.example.threadlocal;

import org.springframework.boot.web.servlet.server.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 应用：数据库连接，session管理
 */
public class ThreadLocalAPP {

    private static String DB_URL = "jdbc:mysql://localhost:3306/test?user=root&amp;password=&amp;useUnicode=true&amp;characterEncoding=utf8&amp;autoReconnect=true";
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>(){
        @Override
        protected Connection initialValue()  {
            try {
                return DriverManager.getConnection(DB_URL);
            }catch (SQLException sqlEx){
                sqlEx.printStackTrace();
                return null;
            }
        }
    };
    private static Connection getConnection(){
        return connectionHolder.get();
    }

    private final static ThreadLocal threadSession = new ThreadLocal();
    public static Session getSession () throws Exception{
        Session session = (Session)threadSession.get();
        if(session == null){
            //hibernate
            //session = getSessionFactory().openSession();
            //threadSession.set(session);
        }
        return session;
    }

}
