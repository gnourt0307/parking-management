/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.Role;
/**
 *
 * @author Admin
 */
public class RoleDAO extends DBContext {
    PreparedStatement stm;
    ResultSet rs;
    
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        try {
            String strSQL = "SELECT * FROM Roles";
            stm = connection.prepareStatement(strSQL);
            rs = stm.executeQuery();
            
            while (rs.next()) {
                int roleID = rs.getInt("RoleID");
                String roleName = rs.getString("RoleName");
                
                Role role = new Role(roleID, roleName);
                roles.add(role);
            }
        } catch (Exception ex) {
            System.out.println("getAllRoles Error: " + ex.getMessage());
        }
        return roles;
    }
}
