/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    public List<models.ActivityLog> getRecentActivities() {
        List<models.ActivityLog> list = new ArrayList<>();
        try {
            // Lấy 10 hoạt động mới nhất bằng cách gộp Tickets và Transactions
            String sql = """
                         SELECT TOP 10 * FROM (
                             SELECT t.EntryTime AS ActivityTime, t.LicensePlate, 'Check-In' AS ActionType, u.FullName AS StaffName
                             FROM Tickets t JOIN Users u ON t.CreatedBy = u.UserID
                             
                             UNION ALL
                             
                             SELECT tr.ExitTime AS ActivityTime, tk.LicensePlate, 'Check-Out' AS ActionType, u.FullName AS StaffName
                             FROM Transactions tr 
                             JOIN Tickets tk ON tr.TicketID = tk.TicketID 
                             JOIN Users u ON tr.StaffID = u.UserID
                         ) AS ActivityLog
                         ORDER BY ActivityTime ASC
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
                log.actionType = rs.getString("ActionType");
                log.staffName = rs.getString("StaffName");
                System.out.println("---- KIỂM TRA DEBUG ----");
                System.out.println("Số lượng Activity lấy được: " + list.size());
                list.add(log);
            }
        } catch (Exception e) {
            System.out.println("getRecentActivities Error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
