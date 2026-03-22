/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import models.Transaction;

/**
 *
 * @author Admin
 */
public class TransactionDAO extends DBContext {
    PreparedStatement stm;
    ResultSet rs;

    /**
     * Tạo hóa đơn cho vé khi xe xuất bãi.
     */
    public boolean createTransaction(int ticketID, BigDecimal totalAmount, int staffID) {
        String sql = """
                    INSERT INTO Transactions (TicketID, ExitTime, TotalAmount, StaffID, CreatedAt)
                    VALUES (?, GETDATE(), ?, ?, GETDATE())
                    """;
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, ticketID);
            stm.setBigDecimal(2, totalAmount);
            stm.setInt(3, staffID);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error in createTransaction: " + e.getMessage());
        
        }
        return false;
    }

    /**
     * Lịch sử các giao dịch (check-out) do 1 staff thực hiện.
     */
    public List<Transaction> getTransactionsByStaff(int staffID) {
        List<Transaction> list = new ArrayList<>();
        String sql = """
                    SELECT *
                    FROM Transactions
                    WHERE StaffID = ?
                    ORDER BY CreatedAt DESC
                    """;
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, staffID);
            rs = stm.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransID(rs.getInt("TransID"));
                t.setTicketID(rs.getInt("TicketID"));
                Timestamp exitTs = rs.getTimestamp("ExitTime");
                if (exitTs != null) {
                    t.setExitTime(exitTs.toLocalDateTime());
                }
                t.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                t.setStaffID(rs.getInt("StaffID"));
                Timestamp createdTs = rs.getTimestamp("CreatedAt");
                if (createdTs != null) {
                    t.setCreatedAt(createdTs.toLocalDateTime());
                }
                list.add(t);
            }
        } catch (Exception e) {
            System.out.println("Error in getTransactionsByStaff: " + e.getMessage());
        
        }
        return list;
    }
}
