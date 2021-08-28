package com.yahya.springwebservice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.spring.guides.gs_spring_web_service.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
public class UserRepository {
    private static final Map<String, User> users = new HashMap<>();

    Connection connection;
    Statement statement;

    public UserRepository(){
        ConnectDB obj_ConnectDB = new ConnectDB();
        connection = obj_ConnectDB.get_connection();
    }

    public void addUser(User user){
        try {
            String query = "insert into users(name,surname,birthdate,gender,email,password) values('"+user.getName()+"','"+user.getSurname()+"','" +user.getBirthdate()+ "','" +user.getGender()+ "','"+user.getEmail()+"','"+user.getPassword()+"')";
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String readUsers(){
        ResultSet rs;
        String str = "";
        try {
            String query ="select * from users";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()){
                str += rs.getString("name") + " - " + rs.getString("surname") + " - " + rs.getString("birthdate") + " - " + rs.getString("gender") + " - " + rs.getString("email");
                str += "|";
            }
            StringBuffer sb= new StringBuffer(str);
            sb.deleteCharAt(sb.length()-1);
            str = String.valueOf(sb);
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    public void updateUser(String oldMail ,User user){
        Assert.notNull(oldMail, "The user's email must not be null");
        String name = user.getName();
        String surname = user.getSurname();
        String gender = user.getGender();
        String mail = user.getEmail();
        String birthdate = user.getBirthdate();
        String password = user.getPassword();

        try {
            String query = "update users set name='"+name+"', surname='"+surname+"', birthdate='"+birthdate+"', gender ='"+gender+"', email ='"+mail+"', password='"+password+"' where email='"+oldMail+"'";
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void deleteUser(String email){
        Assert.notNull(email, "The user's email must not be null");
        try {
            String query = "delete from users where email='"+email+"'";
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkUser(String email,String password){
        ResultSet rs;
        try {
            String query ="select * from users";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()){
                if (Objects.equals(rs.getString("email"), email) && Objects.equals(rs.getString("password"), password)){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean isThereAnyUser(String email){
        ResultSet rs;
        try {
            String query ="select * from users";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()){
                if (Objects.equals(rs.getString("email"), email)){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAdmin(String email, String password) {
        ResultSet rs;
        try {
            String query ="select * from admins";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()){
                if (Objects.equals(rs.getString("username"), email) && Objects.equals(rs.getString("password"), password)){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public boolean contains(String mail) {
        ResultSet rs;
        try {
            String query ="select * from users";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()){
                if (Objects.equals(rs.getString("email"), mail)){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
