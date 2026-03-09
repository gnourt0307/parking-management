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
        String sql = "SELECT z.*, vt.TypeName FROM Zones z LEFT JOIN VehicleTypes vt ON z.TypeID = vt.TypeID";
        try {
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Zone z = new Zone(
                        rs.getInt("ZoneID"),
                        rs.getString("ZoneName"),
                        rs.getString("Description"));
                z.setTypeID(rs.getInt("TypeID"));
                z.setTypeName(rs.getString("TypeName"));
                loadZoneDetails(z);
                zones.add(z);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllZones: " + e.getMessage());
        }
        return zones;
    }

    public List<Zone> getZonesByName(String searchKeyword) {
        List<Zone> zones = new ArrayList<>();
        String sql = "SELECT z.*, vt.TypeName FROM Zones z LEFT JOIN VehicleTypes vt ON z.TypeID = vt.TypeID";

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql += " WHERE z.ZoneName LIKE ?";
        }

        try {
            stm = connection.prepareStatement(sql);
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                stm.setString(1, "%" + searchKeyword.trim() + "%");
            }
            rs = stm.executeQuery();
            while (rs.next()) {
                Zone z = new Zone(
                        rs.getInt("ZoneID"),
                        rs.getString("ZoneName"),
                        rs.getString("Description"));
                z.setTypeID(rs.getInt("TypeID"));
                z.setTypeName(rs.getString("TypeName"));
                loadZoneDetails(z);
                zones.add(z);
            }
        } catch (SQLException e) {
            System.out.println("Error in getZonesByName: " + e.getMessage());
        }
        return zones;
    }

    public List<Zone> getZonesByFilters(String zoneIDStr, String vehicleTypeSearch) {
        List<Zone> zones = new ArrayList<>();
        String sql = "SELECT z.*, vt.TypeName FROM Zones z LEFT JOIN VehicleTypes vt ON z.TypeID = vt.TypeID WHERE 1=1";

        boolean hasZone = zoneIDStr != null && !zoneIDStr.equals("all") && !zoneIDStr.trim().isEmpty();
        if (hasZone) {
            sql += " AND z.ZoneID = ?";
        }

        try {
            stm = connection.prepareStatement(sql);
            if (hasZone) {
                stm.setInt(1, Integer.parseInt(zoneIDStr));
            }
            rs = stm.executeQuery();
            while (rs.next()) {
                Zone z = new Zone(
                        rs.getInt("ZoneID"),
                        rs.getString("ZoneName"),
                        rs.getString("Description"));
                z.setTypeID(rs.getInt("TypeID"));
                z.setTypeName(rs.getString("TypeName"));
                loadZoneDetails(z);
                zones.add(z);
            }
        } catch (SQLException e) {
            System.out.println("Error in getZonesByFilters: " + e.getMessage());
        }

        // Filter by Vehicle Type in Java since it is a derived field
        if (vehicleTypeSearch != null && !vehicleTypeSearch.trim().isEmpty()) {
            String lowerSearch = vehicleTypeSearch.trim().toLowerCase();
            zones.removeIf(
                    z -> z.getTypeName() == null || !z.getTypeName().toLowerCase().contains(lowerSearch));
        }

        return zones;
    }

    private void loadZoneDetails(Zone z) {
        try {
            String capSql = "SELECT COUNT(SlotID) as Capacity FROM Slots WHERE ZoneID = " + z.getZoneID();
            java.sql.Statement stm = connection.createStatement();
            ResultSet capRs = stm.executeQuery(capSql);
            if (capRs.next()) {
                z.setCapacity(capRs.getInt("Capacity"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading zone capacity: " + e.getMessage());
        }
    }

    public boolean checkZoneExist(String zoneName) {
        String sql = "SELECT 1 FROM Zones WHERE ZoneName = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, zoneName.trim());
            rs = stm.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error in checkZoneExist: " + e.getMessage());
        }
        return false;
    }

    public boolean checkZoneExistForUpdate(String zoneName, int zoneID) {
        String sql = "SELECT 1 FROM Zones WHERE ZoneName = ? AND ZoneID != ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, zoneName.trim());
            stm.setInt(2, zoneID);
            rs = stm.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error in checkZoneExistForUpdate: " + e.getMessage());
        }
        return false;
    }

    public boolean addZone(Zone zone) {
        String sql = "INSERT INTO Zones (ZoneName, Description, TypeID) VALUES (?, ?, ?)";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, zone.getZoneName());
            stm.setString(2, zone.getDescription());
            stm.setInt(3, zone.getTypeID());
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error in addZone: " + e.getMessage());
            return false;
        }
    }

    public boolean editZone(Zone zone) {
        String sql = "UPDATE Zones SET ZoneName = ?, Description = ?, TypeID = ? WHERE ZoneID = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, zone.getZoneName());
            stm.setString(2, zone.getDescription());
            stm.setInt(3, zone.getTypeID());
            stm.setInt(4, zone.getZoneID());
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error in editZone: " + e.getMessage());
            return false;
        }
    }

    public String deleteZone(int zoneID) {
        // 1. Check if ANY slot in this zone has an ACTIVE ticket (car currently parked)
        String checkActiveSql = "SELECT 1 FROM Tickets t JOIN Slots s ON t.SlotID = s.SlotID WHERE s.ZoneID = ? AND UPPER(t.Status) = 'ACTIVE'";
        try {
            PreparedStatement checkStm = connection.prepareStatement(checkActiveSql);
            checkStm.setInt(1, zoneID);
            ResultSet checkRs = checkStm.executeQuery();
            if (checkRs.next()) {
                return "Cannot delete zone: There are vehicles currently parked in this zone.";
            }

            // 2. Delete ALL historical tickets associated with this zone's slots
            String deleteTicketsSql = "DELETE FROM Tickets WHERE SlotID IN (SELECT SlotID FROM Slots WHERE ZoneID = ?)";
            PreparedStatement delTicketsStm = connection.prepareStatement(deleteTicketsSql);
            delTicketsStm.setInt(1, zoneID);
            delTicketsStm.executeUpdate();

            // 3. Delete Slots associated with this Zone
            String deleteSlotsSql = "DELETE FROM Slots WHERE ZoneID = ?";
            PreparedStatement delSlotsStm = connection.prepareStatement(deleteSlotsSql);
            delSlotsStm.setInt(1, zoneID);
            delSlotsStm.executeUpdate();

            // 4. Proceed to delete the Zone itself
            String sql = "DELETE FROM Zones WHERE ZoneID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, zoneID);
            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                return "success";
            } else {
                return "Failed to delete zone from the database.";
            }
        } catch (SQLException e) {
            System.out.println("Error in deleteZone: " + e.getMessage());
            return "Internal error occurred during zone deletion.";
        }
    }
}
