/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.CustomerVehicle;
import models.User;
import models.VehicleType;

/**
 *
 * @author Admin
 */
public class CustomerVehicleDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public ArrayList<CustomerVehicle> getCustomerVehicles(User user) {

        ArrayList<CustomerVehicle> customerVehicleList = new ArrayList<>();

        String strSQL = """
                        select c.VehicleID, c.UserID, c.LicensePlate, c.TypeID, v.TypeName 
                        from CustomerVehicles c, VehicleTypes v
                        where c.TypeID = v.TypeID and c.UserID = ?
                        """;
        try {
            stm = connection.prepareStatement(strSQL);
            stm.setInt(1, user.getUserID());

            rs = stm.executeQuery();

            while (rs.next()) {
                int vehicleID = rs.getInt("VehicleID");
                int userID = rs.getInt("UserID");
                int typeID = rs.getInt("TypeID");
                String licensePlate = rs.getString("LicensePlate");
                String typeName = rs.getString("TypeName");

                VehicleType vehicleType = new VehicleType(typeID, typeName);

                CustomerVehicle customerVehicle = new CustomerVehicle(vehicleID, userID, licensePlate, vehicleType);
                customerVehicleList.add(customerVehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerVehicleList;
    }

    public boolean addCustomerVehicle(CustomerVehicle customerVehicle) {
        String strSQL = """
                        insert into CustomerVehicles(UserID, LicensePlate, TypeID) values(?, ?, ?)
                        """;
        try {
            stm = connection.prepareStatement(strSQL);
            stm.setInt(1, customerVehicle.getUserID());
            stm.setString(2, customerVehicle.getLicensePlate());
            stm.setInt(3, customerVehicle.getTypeID());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateCustomerVehicle(int vehicleID, int userID, String licensePlate, int typeID) {
        String strSQL = "UPDATE CustomerVehicles SET LicensePlate = ?, TypeID = ? WHERE VehicleID = ? AND UserID = ?";
        try {
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, licensePlate);
            stm.setInt(2, typeID);
            stm.setInt(3, vehicleID);
            stm.setInt(4, userID);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomerVehicle(int vehicleID, int userID) {
        String strSQL = "DELETE FROM CustomerVehicles WHERE VehicleID = ? AND UserID = ?";
        try {
            stm = connection.prepareStatement(strSQL);
            stm.setInt(1, vehicleID);
            stm.setInt(2, userID);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
