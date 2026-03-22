/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import models.VehicleType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class VehicleTypeDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public List<VehicleType> getAllTypes() {
        List<VehicleType> types = new ArrayList<>();
        String sql = "SELECT * FROM VehicleTypes";
        try {
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                VehicleType t = new VehicleType(
                        rs.getInt("TypeID"),
                        rs.getString("TypeName"));
                types.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllTypes: " + e.getMessage());
        
        }
        return types;
    }

    public int findTypeIdByName(String typeName) {
        String querySql = "SELECT TypeID FROM VehicleTypes WHERE UPPER(TypeName) = UPPER(?)";
        try {
            stm = connection.prepareStatement(querySql);
            stm.setString(1, typeName.trim());
            rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt("TypeID");
            }
        } catch (SQLException e) {
            System.out.println("Error finding VehicleType: " + e.getMessage());
        
        }
        return -1;
    }

    public int createVehicleType(String typeName) {
        String insertSql = "INSERT INTO VehicleTypes (TypeName) VALUES (?)";
        try {
            stm = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, typeName.trim());
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error creating VehicleType: " + e.getMessage());
        
        }
        return -1;
    }

    public int findOrCreateType(String typeName) {
        int typeId = findTypeIdByName(typeName);

        if (typeId != -1) {
            return typeId;
        }

        return createVehicleType(typeName);
    }

    public boolean updateVehicleType(int typeId, String typeName) {
        String sql = "UPDATE VehicleTypes SET TypeName = ? WHERE TypeID = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, typeName.trim());
            stm.setInt(2, typeId);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updateVehicleType: " + e.getMessage());
        
        }
        return false;
    }

    public String deleteVehicleType(int typeId) {
        String sql = "DELETE FROM VehicleTypes WHERE TypeID = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, typeId);
            int rows = stm.executeUpdate();
            return rows > 0 ? "success" : "Vehicle Type not found.";
        } catch (SQLException e) {
            System.out.println("Error deleteVehicleType: " + e.getMessage());
            return "Cannot delete this Vehicle Type because it is being used by existing Zones, Slots, or Tickets.";
        
        }
    }
}
