/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import models.Slot;
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
public class SlotDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public List<Slot> getAllSlots(String searchKeyword, String zoneFilter) {
        List<Slot> slots = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT s.*, z.ZoneName, z.Description as ZoneDescription " +
                "FROM Slots s " +
                "JOIN Zones z ON s.ZoneID = z.ZoneID WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND s.SlotName LIKE ?");
            params.add("%" + searchKeyword.trim() + "%");
        }

        if (zoneFilter != null && !zoneFilter.trim().isEmpty() && !zoneFilter.equals("all")) {
            sql.append(" AND s.ZoneID = ?");
            params.add(Integer.parseInt(zoneFilter));
        }

        sql.append(" ORDER BY z.ZoneName, s.SlotName ASC");

        try {
            stm = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                stm.setObject(i + 1, params.get(i));
            }
            rs = stm.executeQuery();
            while (rs.next()) {
                Slot s = new Slot(
                        rs.getInt("SlotID"),
                        rs.getInt("ZoneID"),
                        rs.getString("SlotName"),
                        rs.getInt("TypeID"),
                        rs.getString("Status"));

                // Map the joined Zone
                Zone z = new Zone(
                        rs.getInt("ZoneID"),
                        rs.getString("ZoneName"),
                        rs.getString("ZoneDescription"));
                s.setZone(z);

                slots.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllSlots: " + e.getMessage());
        }
        return slots;
    }

    public boolean checkSlotExist(int zoneID, String slotName) {
        String sql = "SELECT 1 FROM Slots WHERE ZoneID = ? AND SlotName = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, zoneID);
            stm.setString(2, slotName);
            rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error in checkSlotExist: " + e.getMessage());
        }
        return false;
    }

    public boolean checkSlotExistForUpdate(int zoneID, String slotName, int excludeSlotID) {
        String sql = "SELECT 1 FROM Slots WHERE ZoneID = ? AND SlotName = ? AND SlotID != ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, zoneID);
            stm.setString(2, slotName);
            stm.setInt(3, excludeSlotID);
            rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error in checkSlotExistForUpdate: " + e.getMessage());
        }
        return false;
    }

    public boolean addSlot(int zoneID, String slotName, int typeID, String status) {
        String sql = "INSERT INTO Slots (ZoneID, SlotName, TypeID, Status) VALUES (?, ?, ?, ?)";
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, zoneID);
            stm.setString(2, slotName);
            stm.setInt(3, typeID);
            stm.setString(4, status);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error in addSlot: " + e.getMessage());
        }
        return false;
    }

    public boolean updateSlot(int slotID, int zoneID, String slotName, int typeID, String status) {
        String sql = "UPDATE Slots SET ZoneID = ?, SlotName = ?, TypeID = ?, Status = ? WHERE SlotID = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, zoneID);
            stm.setString(2, slotName);
            stm.setInt(3, typeID);
            stm.setString(4, status);
            stm.setInt(5, slotID);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error in updateSlot: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteSlot(int slotID) {
        String sql = "DELETE FROM Slots WHERE SlotID = ? AND UPPER(Status) != 'OCCUPIED'";
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, slotID);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error in deleteSlot: " + e.getMessage());
        }
        return false;
    }
}
