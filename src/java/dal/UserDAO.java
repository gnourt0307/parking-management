/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.User;

/**
 *
 * @author Admin
 */
public class UserDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public User getUserByUsername(String username) {
        User user = null;

        try {
            String strSQL = """
                            select * from Users s where s.Username = ?
                            """;
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, username);
            rs = stm.executeQuery();

            while (rs.next()) {
                user = new User();
                
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setFullName(rs.getString("FullName"));
                user.setPhone(rs.getString("Phone"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setStatus(rs.getString("Status"));
                Timestamp ts = rs.getTimestamp("CreatedAt");
                if (ts != null) {
                    user.setCreatedAt(ts.toLocalDateTime());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean insertUser(User user) {
        try {
            String strSQL = """
                            insert into Users (Username, Password, FullName, Phone, RoleID) VALUES (?, ?, ?, ?, 3)
                            """;
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getFullName());
            stm.setString(4, user.getPhone());
            
            return stm.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
