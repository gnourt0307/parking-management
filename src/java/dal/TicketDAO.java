/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import models.Pricing;
import models.StaffTicketHistory;
import models.Ticket;
import models.User;

/**
 *
 * @author Admin
 */
public class TicketDAO extends DBContext {
    PreparedStatement stm;
    ResultSet rs;

    public Map<Integer, Ticket> getActiveTicketsMap() {
        Map<Integer, Ticket> map = new HashMap<>();
        // Select active tickets and left join with users to get customer info.
        String sql = "SELECT t.*, u.FullName, u.Phone FROM Tickets t LEFT JOIN Users u ON t.CustomerID = u.UserID WHERE UPPER(t.Status) = 'ACTIVE'";
        try {
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Ticket t = new Ticket();
                t.setTicketID(rs.getInt("TicketID"));
                t.setTicketCode(rs.getString("TicketCode"));
                t.setLicensePlate(rs.getString("LicensePlate"));
                t.setSlotID(rs.getInt("SlotID"));

                java.sql.Timestamp ts = rs.getTimestamp("EntryTime");
                if (ts != null) {
                    t.setEntryTime(ts.toLocalDateTime());
                }
                t.setStatus(rs.getString("Status"));

                // Map customer information if available
                int customerID = rs.getInt("CustomerID");
                if (!rs.wasNull()) {
                    t.setCustomerID(customerID);
                    User customer = new User();
                    customer.setUserID(customerID);
                    customer.setFullName(rs.getString("FullName"));
                    customer.setPhone(rs.getString("Phone"));
                    t.setCustomer(customer);
                }

                map.put(t.getSlotID(), t);
            }
        } catch (Exception e) {
            System.out.println("Error in getActiveTicketsMap: " + e.getMessage());
        }
        return map;
    }

    /**
     * Tìm vé đang ACTIVE theo biển số.
     */
    public Ticket findActiveTicketByPlate(String licensePlate) {
        String sql = """
                    SELECT t.*, u.FullName, u.Phone
                    FROM Tickets t
                    LEFT JOIN Users u ON t.CustomerID = u.UserID
                    WHERE UPPER(t.Status) = 'ACTIVE'
                      AND UPPER(t.LicensePlate) = UPPER(?)
                    """;
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, licensePlate);
            rs = stm.executeQuery();
            if (rs.next()) {
                Ticket t = new Ticket();
                t.setTicketID(rs.getInt("TicketID"));
                t.setTicketCode(rs.getString("TicketCode"));
                t.setLicensePlate(rs.getString("LicensePlate"));
                t.setTypeID(rs.getInt("TypeID"));
                t.setSlotID(rs.getInt("SlotID"));
                java.sql.Timestamp ts = rs.getTimestamp("EntryTime");
                if (ts != null) {
                    t.setEntryTime(ts.toLocalDateTime());
                }
                t.setHourlyRate(rs.getBigDecimal("HourlyRate"));
                t.setDailyRate(rs.getBigDecimal("DailyRate"));
                t.setStatus(rs.getString("Status"));

                int customerID = rs.getInt("CustomerID");
                if (!rs.wasNull()) {
                    t.setCustomerID(customerID);
                    User customer = new User();
                    customer.setUserID(customerID);
                    customer.setFullName(rs.getString("FullName"));
                    customer.setPhone(rs.getString("Phone"));
                    t.setCustomer(customer);
                }
                return t;
            }
        } catch (Exception e) {
            System.out.println("Error in findActiveTicketByPlate: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tạo vé mới khi xe vào bãi.
     * Lấy HourlyRate/DailyRate tại thời điểm tạo từ bảng Pricing.
     */
    public boolean createTicket(Ticket t) {
        String sql = """
                    INSERT INTO Tickets
                      (TicketCode, LicensePlate, TypeID, SlotID, CustomerID,
                       EntryTime, HourlyRate, DailyRate, Status, CreatedBy)
                    VALUES (?, ?, ?, ?, ?, GETDATE(), ?, ?, 'ACTIVE', ?)
                    """;
        try {
            PricingDAO pDAO = new PricingDAO();
            Pricing pricing = pDAO.getPricingByTypeId(t.getTypeID());
            if (pricing == null) {
                return false;
            }

            stm = connection.prepareStatement(sql);
            stm.setString(1, t.getTicketCode());
            stm.setString(2, t.getLicensePlate());
            stm.setInt(3, t.getTypeID());
            stm.setInt(4, t.getSlotID());
            if (t.getCustomerID() == null) {
                stm.setNull(5, java.sql.Types.INTEGER);
            } else {
                stm.setInt(5, t.getCustomerID());
            }
            stm.setBigDecimal(6, pricing.getHourlyRate());
            stm.setBigDecimal(7, pricing.getDailyRate());
            stm.setInt(8, t.getCreatedBy());

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error in createTicket: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy chi tiết vé theo ID.
     */
    public Ticket getTicketById(int ticketID) {
        String sql = "SELECT * FROM Tickets WHERE TicketID = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, ticketID);
            rs = stm.executeQuery();
            if (rs.next()) {
                Ticket t = new Ticket();
                t.setTicketID(rs.getInt("TicketID"));
                t.setTicketCode(rs.getString("TicketCode"));
                t.setLicensePlate(rs.getString("LicensePlate"));
                t.setTypeID(rs.getInt("TypeID"));
                t.setSlotID(rs.getInt("SlotID"));
                java.sql.Timestamp ts = rs.getTimestamp("EntryTime");
                if (ts != null) {
                    t.setEntryTime(ts.toLocalDateTime());
                }
                t.setHourlyRate(rs.getBigDecimal("HourlyRate"));
                t.setDailyRate(rs.getBigDecimal("DailyRate"));
                t.setStatus(rs.getString("Status"));
                t.setCreatedBy(rs.getInt("CreatedBy"));
                return t;
            }
        } catch (Exception e) {
            System.out.println("Error in getTicketById: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật trạng thái vé.
     */
    public boolean updateTicketStatus(int ticketID, String status) {
        String sql = "UPDATE Tickets SET Status = ? WHERE TicketID = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, status);
            stm.setInt(2, ticketID);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error in updateTicketStatus: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lịch sử gộp check-in / check-out của một staff,
     * có lọc theo mã vé hoặc biển số (tùy chọn).
     */
    public List<StaffTicketHistory> getStaffHistory(int staffID, String searchKeyword) {
        List<StaffTicketHistory> list = new ArrayList<>();

        String baseFilter = "";
        boolean hasSearch = searchKeyword != null && !searchKeyword.trim().isEmpty();
        if (hasSearch) {
            baseFilter = " AND (t.TicketCode LIKE ? OR t.LicensePlate LIKE ?) ";
        }

        String sql = """
                    SELECT * FROM (
                        -- CHECK-IN records
                        SELECT
                            t.TicketCode,
                            t.LicensePlate,
                            vt.TypeName       AS VehicleType,
                            t.EntryTime       AS EntryTime,
                            NULL              AS ExitTime,
                            NULL              AS TotalAmount,
                            'CHECK-IN'        AS ActionType,
                            t.Status          AS Status
                        FROM Tickets t
                        JOIN VehicleTypes vt ON t.TypeID = vt.TypeID
                        WHERE t.CreatedBy = ?
                    """ + baseFilter + """
                        UNION ALL
                        -- CHECK-OUT records
                        SELECT
                            t.TicketCode,
                            t.LicensePlate,
                            vt.TypeName       AS VehicleType,
                            t.EntryTime       AS EntryTime,
                            tr.ExitTime       AS ExitTime,
                            tr.TotalAmount    AS TotalAmount,
                            'CHECK-OUT'       AS ActionType,
                            t.Status          AS Status
                        FROM Tickets t
                        JOIN VehicleTypes vt ON t.TypeID = vt.TypeID
                        JOIN Transactions tr ON tr.TicketID = t.TicketID
                        WHERE tr.StaffID = ?
                    """ + baseFilter + """
                    ) AS X
                    ORDER BY
                        COALESCE(X.ExitTime, X.EntryTime) DESC
                    """;

        try {
            stm = connection.prepareStatement(sql);
            int idx = 1;
            // Parameters for CHECK-IN block
            stm.setInt(idx++, staffID);
            if (hasSearch) {
                String like = "%" + searchKeyword.trim() + "%";
                stm.setString(idx++, like);
                stm.setString(idx++, like);
            }
            // Parameters for CHECK-OUT block
            stm.setInt(idx++, staffID);
            if (hasSearch) {
                String like = "%" + searchKeyword.trim() + "%";
                stm.setString(idx++, like);
                stm.setString(idx++, like);
            }

            rs = stm.executeQuery();
            while (rs.next()) {
                StaffTicketHistory h = new StaffTicketHistory();
                h.setTicketCode(rs.getString("TicketCode"));
                h.setLicensePlate(rs.getString("LicensePlate"));
                h.setVehicleType(rs.getString("VehicleType"));
                java.sql.Timestamp entryTs = rs.getTimestamp("EntryTime");
                if (entryTs != null) {
                    h.setEntryTime(entryTs.toLocalDateTime());
                }
                java.sql.Timestamp exitTs = rs.getTimestamp("ExitTime");
                if (exitTs != null) {
                    h.setExitTime(exitTs.toLocalDateTime());
                }
                h.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                h.setActionType(rs.getString("ActionType"));
                h.setStatus(rs.getString("Status"));
                list.add(h);
            }
        } catch (Exception e) {
            System.out.println("Error in getStaffHistory: " + e.getMessage());
        }

        return list;
    }
}
