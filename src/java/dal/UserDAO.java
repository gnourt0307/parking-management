/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import models.Role;
import models.User;

/**
 *
 * @author Admin
 */
public class UserDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public List<User> getListUsers() {
        List<User> users = new ArrayList<>();
        try {
            String strSQL = """
                            SELECT U.*, R.RoleName
                            FROM Users U JOIN Roles R ON U.RoleID = R.RoleID
                            """;

            stm = connection.prepareCall(strSQL);
            rs = stm.executeQuery();
            while (rs.next()) {
                int userID = rs.getInt("UserID");
                String userName = rs.getString("Username");
                String password = rs.getString("Password");
                String fullName = rs.getString("Fullname");
                String phone = rs.getString("Phone");
                int roleID = rs.getInt("RoleID");
                String status = rs.getString("Status");
                java.sql.Timestamp sqlTimestamp = rs.getTimestamp("CreatedAt");
                LocalDateTime createdAt = sqlTimestamp.toLocalDateTime();
                String roleName  = rs.getString("RoleName");
                Role role = new Role(roleID, roleName);
                User user = new User(userID, userName, password, fullName, phone, roleID, status, createdAt, role);
                users.add(user);
            }
        } catch (Exception ex) {
            System.out.println("GetAccounts:" + ex.getMessage());
        }
        return users;
    }
    
    public List<User> getListUsersBySearch(String searchKeyword) {
        List<User> users = new ArrayList<>();
        try {
            // Dùng LIKE để tìm kiếm chuỗi chứa từ khóa
            String strSQL = """
                            SELECT U.*, R.RoleName
                            FROM Users U JOIN Roles R ON U.RoleID = R.RoleID
                            WHERE U.Username LIKE ?
                            """;

            stm = connection.prepareStatement(strSQL);
            stm.setString(1, "%" + searchKeyword + "%"); // Truyền từ khóa vào
            rs = stm.executeQuery();
            
            while (rs.next()) {
                int userID = rs.getInt("UserID");
                String userName = rs.getString("Username");
                String password = rs.getString("Password");
                String fullName = rs.getString("Fullname");
                String phone = rs.getString("Phone");
                int roleID = rs.getInt("RoleID");
                String status = rs.getString("Status");
                
                java.sql.Timestamp sqlTimestamp = rs.getTimestamp("CreatedAt");
                LocalDateTime createdAt = (sqlTimestamp != null) ? sqlTimestamp.toLocalDateTime() : null;
                
                String roleName  = rs.getString("RoleName");
                Role role = new Role(roleID, roleName);
                
                User user = new User(userID, userName, password, fullName, phone, roleID, status, createdAt, role);
                users.add(user);
            }
        } catch (Exception ex) {
            System.out.println("getListUsersBySearch Error: " + ex.getMessage());
        }
        return users;
    }

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
                            insert into Users (Username, Password, FullName, Phone, RoleID, Status) VALUES (?, ?, ?, ?, ?, ?)
                            """;
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getFullName());
            stm.setString(4, user.getPhone());
            stm.setInt(5, user.getRoleID()); // Lấy RoleID động
            stm.setString(6, user.getStatus()); // Lấy Status động
            return stm.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
