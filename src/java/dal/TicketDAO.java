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
import models.Slot;
import models.StaffTicketHistory;
import models.Ticket;
import models.User;
import models.Zone;

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

    public List<Ticket> getActiveTicketsList() {
        List<Ticket> list = new ArrayList<>();
        String sql = """
                    SELECT
                      t.*,
                      u.FullName, u.Phone,
                      s.SlotName,
                      z.ZoneName,
                      vt.TypeName
                    FROM Tickets t
                    LEFT JOIN Users u ON t.CustomerID = u.UserID
                    JOIN Slots s ON t.SlotID = s.SlotID
                    JOIN Zones z ON s.ZoneID = z.ZoneID
                    JOIN VehicleTypes vt ON t.TypeID = vt.TypeID
                    WHERE UPPER(t.Status) = 'ACTIVE'
                    ORDER BY t.EntryTime DESC
                    """;
        try {
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Ticket t = new Ticket();
                t.setTicketID(rs.getInt("TicketID"));
                t.setTicketCode(rs.getString("TicketCode"));
                t.setLicensePlate(rs.getString("LicensePlate"));
                t.setTypeID(rs.getInt("TypeID"));
                
                models.VehicleType vt = new models.VehicleType(rs.getInt("TypeID"), rs.getString("TypeName"));
                t.setVehicleType(vt);
                
                t.setSlotID(rs.getInt("SlotID"));
                java.sql.Timestamp ts = rs.getTimestamp("EntryTime");
                if (ts != null) {
                    t.setEntryTime(ts.toLocalDateTime());
                }
                
                Slot slot = new Slot();
                slot.setSlotID(t.getSlotID());
                slot.setSlotName(rs.getString("SlotName"));
                Zone zone = new Zone();
                zone.setZoneName(rs.getString("ZoneName"));
                slot.setZone(zone);
                t.setSlot(slot);
                
                list.add(t);
            }
        } catch (Exception e) {
            System.out.println("Error in getActiveTicketsList: " + e.getMessage());
        
        }
        return list;
    }

    /**
     * Tim ve dang ACTIVE theo bien so.
     */
    public Ticket findActiveTicketByPlate(String licensePlate) {
        String sql = """
                    SELECT
                      t.*,
                      u.FullName, u.Phone,
                      s.SlotName,
                      z.ZoneName
                    FROM Tickets t
                    LEFT JOIN Users u ON t.CustomerID = u.UserID
                    JOIN Slots s ON t.SlotID = s.SlotID
                    JOIN Zones z ON s.ZoneID = z.ZoneID
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

                // Slot + Zone for invoice display
                Slot slot = new Slot();
                slot.setSlotID(t.getSlotID());
                slot.setSlotName(rs.getString("SlotName"));
                Zone zone = new Zone();
                zone.setZoneName(rs.getString("ZoneName"));
                slot.setZone(zone);
                t.setSlot(slot);

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
     * Tao ve moi khi xe vao bai.
     * Lay HourlyRate/DailyRate tai th�?i diem tao tu bang Pricing.
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
     * Lay chi tiet ve theo ID.
     */
    public Ticket getTicketById(int ticketID) {
        String sql = """
                    SELECT
                      t.*,
                      s.SlotName,
                      z.ZoneName
                    FROM Tickets t
                    JOIN Slots s ON t.SlotID = s.SlotID
                    JOIN Zones z ON s.ZoneID = z.ZoneID
                    WHERE t.TicketID = ?
                    """;
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

                Slot slot = new Slot();
                slot.setSlotID(t.getSlotID());
                slot.setSlotName(rs.getString("SlotName"));
                Zone zone = new Zone();
                zone.setZoneName(rs.getString("ZoneName"));
                slot.setZone(zone);
                t.setSlot(slot);
                return t;
            }
        } catch (Exception e) {
            System.out.println("Error in getTicketById: " + e.getMessage());
        
        }
        return null;
    }

    /**
     * Cap nhat trang thai ve.
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
     * Tao TicketCode theo format: VEX-yyMMdd-0001 (tang dan theo ngay).
     */
    public String generateNextTicketCode() {
        String sql = """
                    SELECT MAX(TicketCode) AS MaxCode
                    FROM Tickets
                    WHERE TicketCode LIKE ('VEX-' + FORMAT(GETDATE(), 'yyMMdd') + '-%')
                    """;
        try {
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            int nextSeq = 1;
            if (rs.next()) {
                String maxCode = rs.getString("MaxCode");
                if (maxCode != null && maxCode.length() >= 4) {
                    String last4 = maxCode.substring(maxCode.length() - 4);
                    try {
                        nextSeq = Integer.parseInt(last4) + 1;
                    } catch (NumberFormatException ignored) {
                        nextSeq = 1;
                    
        }
                }
            }
            String datePart = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMMdd"));
            return "VEX-" + datePart + "-" + String.format("%04d", nextSeq);
        } catch (Exception e) {
            System.out.println("Error in generateNextTicketCode: " + e.getMessage());
        
        }
        // Fallback neu co loi DB
        String datePart = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMMdd"));
        return "VEX-" + datePart + "-" + String.format("%04d", (int) (System.currentTimeMillis() % 10000));
    }

    /**
     * Lich su gop check-in / check-out cua mot staff,
     * co l�?c theo ma ve hoac bien so (tuy ch�?n).
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

    public List<Ticket> getTicketHistoryByCustomerID(int customerID) {
        List<Ticket> list = new ArrayList<>();
        String sql = "SELECT t.*, vt.TypeName, s.SlotName, z.ZoneName, " +
                     "tr.TransID, tr.ExitTime, tr.TotalAmount " +
                     "FROM Tickets t " +
                     "JOIN VehicleTypes vt ON t.TypeID = vt.TypeID " +
                     "JOIN Slots s ON t.SlotID = s.SlotID " +
                     "JOIN Zones z ON s.ZoneID = z.ZoneID " +
                     "LEFT JOIN Transactions tr ON t.TicketID = tr.TicketID " +
                     "WHERE t.CustomerID = ? ORDER BY t.EntryTime DESC";
        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, customerID);
            rs = stm.executeQuery();
            while (rs.next()) {
                Ticket t = new Ticket();
                t.setTicketID(rs.getInt("TicketID"));
                t.setTicketCode(rs.getString("TicketCode"));
                t.setLicensePlate(rs.getString("LicensePlate"));
                t.setTypeID(rs.getInt("TypeID"));
                t.setSlotID(rs.getInt("SlotID"));
                t.setCustomerID(rs.getInt("CustomerID"));
                
                java.sql.Timestamp entryTs = rs.getTimestamp("EntryTime");
                if (entryTs != null) {
                    t.setEntryTime(entryTs.toLocalDateTime());
                }
                t.setHourlyRate(rs.getBigDecimal("HourlyRate"));
                t.setDailyRate(rs.getBigDecimal("DailyRate"));
                t.setStatus(rs.getString("Status"));
                
                models.VehicleType vt = new models.VehicleType(rs.getInt("TypeID"), rs.getString("TypeName"));
                t.setVehicleType(vt);
                
                models.Slot s = new models.Slot();
                s.setSlotID(rs.getInt("SlotID"));
                s.setSlotName(rs.getString("SlotName"));
                t.setSlot(s);
                
                int transID = rs.getInt("TransID");
                if (!rs.wasNull()) {
                    models.Transaction tr = new models.Transaction();
                    tr.setTransID(transID);
                    tr.setTicketID(rs.getInt("TicketID"));
                    java.sql.Timestamp exitTs = rs.getTimestamp("ExitTime");
                    if (exitTs != null) {
                        tr.setExitTime(exitTs.toLocalDateTime());
                    }
                    tr.setTotalAmount(rs.getBigDecimal("TotalAmount"));
                    // Removed payment method mapping
                    t.setTransaction(tr);
                }
                
                list.add(t);
            }
        } catch (Exception e) {
            System.out.println("Error in getTicketHistoryByCustomerID: " + e.getMessage());
        
        }
        return list;
    }
}
