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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import models.Pricing;
import models.Report;
import models.VehicleType;

/**
 *
 * @author Admin
 */
public class ReportDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public ArrayList<Report> getDailyReport(List<VehicleType> vehicleList, String startDate, String endDate) {
        ArrayList<Report> reportList = new ArrayList<>();

        try {
            StringBuilder sumQuery = new StringBuilder();
            StringBuilder tickets = new StringBuilder();

            for (VehicleType v : vehicleList) {
                sumQuery.append(
                        "SUM(CASE WHEN vt.TypeName = '")
                        .append(v.getTypeName())
                        .append("' THEN tr.TotalAmount ELSE 0 END) AS [")
                        .append(v.getTypeName())
                        .append("], ");

                tickets.append(
                        "SUM(CASE WHEN vt.TypeName = '")
                        .append(v.getTypeName())
                        .append("' THEN 1 ELSE 0 END) AS [")
                        .append(v.getTypeName()).append("Tickets")
                        .append("], ");
            }
            
            System.out.println(tickets);

            String strSQL = """
                            WITH TopRevenue AS (
                                    SELECT 
                                        Date,
                                        TypeName AS TopVehicleType,
                                        Total AS TopRevenue
                                    FROM (
                                        SELECT 
                                            CAST(tr.ExitTime AS DATE) AS Date,
                                            vt.TypeName,
                                            SUM(tr.TotalAmount) AS Total,
                                            ROW_NUMBER() OVER (
                                                PARTITION BY CAST(tr.ExitTime AS DATE)
                                                ORDER BY SUM(tr.TotalAmount) DESC
                                            ) AS rn
                                        FROM Transactions tr
                                        JOIN Tickets t ON tr.TicketID = t.TicketID
                                        JOIN VehicleTypes vt ON t.TypeID = vt.TypeID
                                        WHERE CAST(tr.ExitTime AS DATE) 
                                              BETWEEN ? AND ?
                                        GROUP BY 
                                            CAST(tr.ExitTime AS DATE),
                                            vt.TypeName
                                    ) x
                                    WHERE rn = 1
                                ),
                                
                                DailySummary AS (
                                    SELECT 
                                        CAST(tr.ExitTime AS DATE) AS [Date],
                                        COUNT(t.TicketID) AS TotalTickets,
                            """ + tickets + sumQuery + """
                                             SUM(tr.TotalAmount) AS TotalRevenue
                                                 FROM Transactions tr
                                                 JOIN Tickets t ON tr.TicketID = t.TicketID
                                                 JOIN VehicleTypes vt ON t.TypeID = vt.TypeID
                                                 WHERE CAST(tr.ExitTime AS DATE) 
                                                       BETWEEN ? AND ?
                                                 GROUP BY CAST(tr.ExitTime AS DATE)
                                             )
                                             
                                             SELECT 
                                                 d.*,
                                                 t.TopVehicleType,
                                                 t.TopRevenue
                                             FROM DailySummary d
                                             LEFT JOIN TopRevenue t 
                                                 ON d.Date = t.Date
                                             ORDER BY d.Date DESC;
                                             """;
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, startDate);
            stm.setString(2, endDate);
            stm.setString(3, startDate);
            stm.setString(4, endDate);
            rs = stm.executeQuery();

            while (rs.next()) {
                Report report = new Report();
                ArrayList<BigDecimal> vehicleRevenueList = report.getVehicleRevenueList();
                ArrayList<String> vehicleTypeList = report.getVehicleTypeList();
                ArrayList<Integer> totalTicketsByVehicleType = report.getTotalTicketsByVehicleType();
                Date date = rs.getDate("Date");
                int totalTickets = rs.getInt("TotalTickets");
                BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");
                BigDecimal topRevenue = rs.getBigDecimal("TopRevenue");
                String topVehicleType = rs.getString("TopVehicleType");

                for (VehicleType v : vehicleList) {
                    BigDecimal vehicleRevenue = rs.getBigDecimal(v.getTypeName());
                    int ticketsPerVehicle = rs.getInt(v.getTypeName() + "Tickets");
                    vehicleRevenueList.add(vehicleRevenue);
                    vehicleTypeList.add(v.getTypeName());
                    totalTicketsByVehicleType.add(ticketsPerVehicle);
                    
                    System.out.println(v.getTypeName());
                    System.out.println(ticketsPerVehicle);
                    System.out.println(vehicleRevenue);
                    System.out.println("-------------");
                }

                report.setDate(date);
                report.setTotalTickets(totalTickets);
                report.setTotalAmount(totalRevenue);
                report.setTopRevenue(topRevenue);
                report.setTopVehicle(topVehicleType);
                reportList.add(report);
            }

            return reportList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Report> getMonthlyReport(List<VehicleType> vehicleList, String startDate, String endDate) {
        ArrayList<Report> reportList = new ArrayList<>();

        try {

            StringBuilder sumQuery = new StringBuilder();
            StringBuilder vehicleName = new StringBuilder();
            StringBuilder tickets = new StringBuilder();

            for (VehicleType v : vehicleList) {
                sumQuery.append(
                        "SUM(CASE WHEN vt.TypeName = '")
                        .append(v.getTypeName())
                        .append("' THEN tr.TotalAmount ELSE 0 END) AS [")
                        .append(v.getTypeName())
                        .append("], ");
                vehicleName.append("m.[").append(v.getTypeName()).append("],");

                tickets.append(
                        "SUM(CASE WHEN vt.TypeName = '")
                        .append(v.getTypeName())
                        .append("' THEN 1 ELSE 0 END) AS [")
                        .append(v.getTypeName() + "Tickets")
                        .append("], ");
            }

            String strSQL = """
                            WITH TopRevenue AS (
                                            SELECT 
                                                FORMAT(tr.ExitTime, 'yyyy-MM') AS [Month],
                                                vt.TypeName AS TopVehicleType,
                                                SUM(tr.TotalAmount) AS TopRevenue,
                                                ROW_NUMBER() OVER (
                                                    PARTITION BY FORMAT(tr.ExitTime, 'yyyy-MM')
                                                    ORDER BY SUM(tr.TotalAmount) DESC
                                                ) AS rn
                                            FROM Transactions tr
                                            JOIN Tickets t ON tr.TicketID = t.TicketID
                                            JOIN VehicleTypes vt ON t.TypeID = vt.TypeID
                                            WHERE CAST(tr.ExitTime AS DATE) 
                                                  BETWEEN ? AND ?
                                            GROUP BY 
                                                FORMAT(tr.ExitTime, 'yyyy-MM'),
                                                vt.TypeName
                                        ),
                                        
                                        MonthlySummary AS (
                                            SELECT 
                                                FORMAT(tr.ExitTime, 'yyyy-MM') AS [Month],
                                        
                                                COUNT(t.TicketID) AS TotalTickets,
                            """ + tickets + sumQuery + """
                                             SUM(tr.TotalAmount) AS TotalRevenue
                                                 
                                                     FROM Transactions tr
                                                     JOIN Tickets t ON tr.TicketID = t.TicketID
                                                     JOIN VehicleTypes vt ON t.TypeID = vt.TypeID
                                                     WHERE CAST(tr.ExitTime AS DATE) 
                                                           BETWEEN ? AND ?
                                                     GROUP BY FORMAT(tr.ExitTime, 'yyyy-MM')
                                                 )
                                                 
                                                 SELECT 
                                                     m.Month,
                                                     m.TotalTickets,
                                                 """
                    + vehicleName
                    + """
                                                     m.TotalRevenue,
                                                     t.TopVehicleType,
                                                     t.TopRevenue
                                                 FROM MonthlySummary m
                                                 LEFT JOIN TopRevenue t 
                                                     ON m.Month = t.Month
                                                     AND t.rn = 1
                                                 ORDER BY m.Month DESC;
                                             """;
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, startDate);
            stm.setString(2, endDate);
            stm.setString(3, startDate);
            stm.setString(4, endDate);
            rs = stm.executeQuery();

            while (rs.next()) {
                Report report = new Report();
                ArrayList<BigDecimal> vehicleRevenueList = report.getVehicleRevenueList();
                ArrayList<String> vehicleTypeList = report.getVehicleTypeList();
                ArrayList<Integer> totalTicketsByVehicleType = report.getTotalTicketsByVehicleType();
                String month = rs.getString("month");
                int totalTickets = rs.getInt("TotalTickets");
                BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");
                BigDecimal topRevenue = rs.getBigDecimal("TopRevenue");
                String topVehicleType = rs.getString("TopVehicleType");

                for (VehicleType v : vehicleList) {
                    BigDecimal vehicleRevenue = rs.getBigDecimal(v.getTypeName());
                    int ticketsPerVehicle = rs.getInt(v.getTypeName() + "Tickets");
                    vehicleRevenueList.add(vehicleRevenue);
                    vehicleTypeList.add(v.getTypeName());
                    totalTicketsByVehicleType.add(ticketsPerVehicle);
                    
                    System.out.println(v.getTypeName());
                    System.out.println(ticketsPerVehicle);
                    System.out.println(vehicleRevenue);
                    System.out.println("-------------");
                }

                report.setMonth(month);
                report.setTotalTickets(totalTickets);
                report.setTotalAmount(totalRevenue);
                report.setTopRevenue(topRevenue);
                report.setTopVehicle(topVehicleType);
                reportList.add(report);
            }

            return reportList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Report getTotalAmountAndTickets(String startDate, String endDate) {
        try {
            String strSQL = """
                            select sum(TotalAmount)  as TotalAmount, count(TicketID) as totalTickets from Transactions tr WHERE CAST(tr.ExitTime AS DATE) BETWEEN ? AND ?
                            """;

            stm = connection.prepareStatement(strSQL);
            stm.setString(1, startDate);
            stm.setString(2, endDate);
            rs = stm.executeQuery();
            Report report = new Report();

            while (rs.next()) {

                BigDecimal totalAmount = rs.getBigDecimal("totalAmount");
                int totalTickets = rs.getInt("totalTickets");

                report.setTotalAmount(totalAmount);
                report.setTotalTickets(totalTickets);

            }

            return report;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Report getTopVehicleTypeRevenue(String startDate, String endDate) {
        Report report = new Report();

        try {
            String strSQL = """
                            select top 1 * from (
                            	SELECT 
                            		vt.TypeName,
                            		SUM(tr.TotalAmount) AS Total
                            	FROM Transactions tr
                            	JOIN Tickets t ON tr.TicketID = t.TicketID
                            	JOIN VehicleTypes vt ON t.TypeID = vt.TypeID
                            	WHERE CAST(tr.ExitTime AS DATE) BETWEEN ? AND ?
                            	GROUP BY vt.TypeName
                            ) as Revenue order by Total desc 
                            """;

            stm = connection.prepareStatement(strSQL);
            stm.setString(1, startDate);
            stm.setString(2, endDate);

            rs = stm.executeQuery();

            while (rs.next()) {
                String typeName = rs.getString("TypeName");
                BigDecimal revenue = rs.getBigDecimal("Total");

                report.setTopVehicle(typeName);
                report.setTopRevenue(revenue);
            }

            return report;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
