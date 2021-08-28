package com.yahya.springwebservice;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import io.spring.guides.gs_spring_web_service.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageRepository {

    Connection connection;
    Statement statement;

    public MessageRepository(){
        ConnectDB obj_ConnectDB = new ConnectDB();
        connection = obj_ConnectDB.get_connection();
    }

    public void addMessage(String from,String to,String body){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            String query = "insert into messages(msgfrom,msgto,message,date) values('"+from+"','"+to+"','" +body+ "','"+dtf.format(now)+"')";
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String read_inbox(String login) {
        ResultSet rs;
        String str = "";
        try {
            String query = "select * from messages where msgto='" + login + "'";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()) {
                str += rs.getString("date") + " - " + rs.getString("msgfrom") + ": " + rs.getString("message");
                str += "|";
            }
            if (str.equals("")){
                return "";
            }
            StringBuffer sb= new StringBuffer(str);
            sb.deleteCharAt(sb.length()-1);
            str = String.valueOf(sb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    public String read_outbox(String login) {
        ResultSet rs;
        String str = "";
        try {
            String query = "select * from messages where msgfrom='" + login + "'";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()) {
                str += rs.getString("date") + " - " + rs.getString("msgfrom") + ": " + rs.getString("message");
                str += "|";
            }
            if (str.equals("")){
                return "|";
            }
            StringBuffer sb= new StringBuffer(str);
            sb.deleteCharAt(sb.length()-1);
            str = String.valueOf(sb);;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public boolean isThereAnyMessageContainsMail(String mail) {
        ResultSet rs;
        try {
            String query ="select * from messages";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()){
                if (Objects.equals(rs.getString("msgfrom"), mail) || Objects.equals(rs.getString("msgto"), mail)){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void updateMessages(String oldMail, String newMail) {
        try {
            String query = "update messages set msgfrom='"+newMail+"' where msgfrom='"+oldMail+"'";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            String kuery = "update messages set msgto='"+newMail+"' where msgto='"+oldMail+"'";
            statement = connection.createStatement();
            statement.executeUpdate(kuery);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}