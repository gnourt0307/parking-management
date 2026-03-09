/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import models.Zone;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ZoneDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public List<Zone> getAllZones() {
        List<Zone> zones = new ArrayList<>();
        String sql = "SELECT * FROM Zones";
        try {
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Zone z = new Zone(
                        rs.getInt("ZoneID"),
                        rs.getString("ZoneName"),
                        rs.getString("Description"));
                zones.add(z);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllZones: " + e.getMessage());
        }
        return zones;
    }
}
