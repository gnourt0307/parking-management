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
}
