/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.ActivityLog;

/**
 *
 * @author tuant
 */
public class DashboardDAO extends DBContext {
    PreparedStatement stm;
    ResultSet rs;
    
    public int getTotalCapacity() {
        int total = 0;
        try {
            // Đếm tổng số lượng ID trong bảng Slots
            String sql = "SELECT COUNT(SlotID) as Total FROM Slots";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                total = rs.getInt("Total");
            }
        } catch (SQLException e) {
            System.out.println("getTotalCapacity Error: " + e.getMessage());
        }
        return total;
    }
    
    public int getOccupiedSlotsCount() {
        int count = 0;
        try {
            // Đếm các slot có trạng thái là Occupied (dùng UPPER để không phân biệt hoa thường)
            String sql = "SELECT COUNT(SlotID) as OccupiedCount FROM Slots WHERE UPPER(Status) = 'OCCUPIED'";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("OccupiedCount");
            }
        } catch (SQLException e) {
            System.out.println("getOccupiedSlotsCount Error: " + e.getMessage());
        }
        return count;
    }
    
    public int getAvailableSlotsCount() {
        int count = 0;
        try {
            // Đếm các slot có trạng thái chính xác là Available
            String sql = "SELECT COUNT(SlotID) as AvailableCount FROM Slots WHERE UPPER(Status) = 'AVAILABLE'";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("AvailableCount");
            }
        } catch (SQLException e) {
            System.out.println("getAvailableSlotsCount Error: " + e.getMessage());
        }
        return count;
    }
    
    public List<models.ActivityLog> getRecentActivities() {
        List<models.ActivityLog> list = new ArrayList<>();
        try {
            // Lấy 10 hoạt động mới nhất bằng cách gộp Tickets và Transactions
            String sql = """
                         SELECT TOP 15 * FROM (
                             SELECT t.EntryTime AS ActivityTime, t.LicensePlate, vt.TypeName AS VehicleType, s.SlotName, z.ZoneName, 'Check-In' AS ActionType, u.FullName AS StaffName
                             FROM Tickets t 
                             JOIN Users u ON t.CreatedBy = u.UserID
                             JOIN Slots s ON t.SlotID = s.SlotID
                             JOIN Zones z ON s.ZoneID = z.ZoneID
                             JOIN VehicleTypes vt ON t.TypeID = vt.TypeID
                             
                             UNION ALL
                             
                             SELECT tr.ExitTime AS ActivityTime, tk.LicensePlate, vt.TypeName AS VehicleType, s.SlotName, z.ZoneName, 'Check-Out' AS ActionType, u.FullName AS StaffName
                             FROM Transactions tr 
                             JOIN Tickets tk ON tr.TicketID = tk.TicketID 
                             JOIN Users u ON tr.StaffID = u.UserID
                             JOIN Slots s ON tk.SlotID = s.SlotID
                             JOIN Zones z ON s.ZoneID = z.ZoneID
                             JOIN VehicleTypes vt ON tk.TypeID = vt.TypeID
                         ) AS ActivityLog
                         ORDER BY ActivityTime DESC
                         """;
            
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            
            while (rs.next()) {
                ActivityLog log = new ActivityLog();
                java.sql.Timestamp ts = rs.getTimestamp("ActivityTime");
                if (ts != null) {
                    log.activityTime = ts.toLocalDateTime();
                }
                log.licensePlate = rs.getString("LicensePlate");
                log.vehicleType = rs.getString("VehicleType");
                log.slot = rs.getString("SlotName");
                log.zone = rs.getString("ZoneName");
                log.actionType = rs.getString("ActionType");
                log.staffName = rs.getString("StaffName");
                list.add(log);
            }
        } catch (Exception e) {
            System.out.println("getRecentActivities Error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    public BigDecimal getTodaysRevenue() {
        BigDecimal revenue = null;
        try {
            // Lấy tổng tiền của các giao dịch có ExitTime trong ngày hôm nay
            String sql = """
                         SELECT SUM(TotalAmount) AS Revenue 
                         FROM Transactions 
                         WHERE CAST(ExitTime AS DATE) = CAST(GETDATE() AS DATE)
                         """;
            
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                revenue = rs.getBigDecimal("Revenue");
            }
        } catch (Exception e) {
            System.out.println("getTodaysRevenue Error: " + e.getMessage());
        }
        return revenue;
    }
}
