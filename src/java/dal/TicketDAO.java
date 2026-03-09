/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
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
}
