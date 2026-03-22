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
import java.util.List;
import models.Pricing;
import models.VehicleType;

/**
 *
 * @author Admin
 */
public class PricingDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public ArrayList<Pricing> getPricingList() {
        ArrayList<Pricing> pricingList = new ArrayList<>();
        
        try {
            String strSQL = """
                            select p.PricingID, p.TypeID, p.HourlyRate, p.DailyRate, vt.TypeName from Pricing p, VehicleTypes vt where p.TypeID = vt.TypeID;
                            """;
            stm = connection.prepareStatement(strSQL);
            rs = stm.executeQuery();

            while (rs.next()) {
                int pricingID = rs.getInt("pricingID");
                int typeID = rs.getInt("TypeID");
                BigDecimal hourlyRate = rs.getBigDecimal("HourlyRate");
                BigDecimal dailyRate = rs.getBigDecimal("DailyRate");
                String typeName = rs.getString("TypeName");

                Pricing pricing = new Pricing(pricingID, typeID, hourlyRate, dailyRate);
                pricing.setVehicleType(new VehicleType(typeID, typeName));

                pricingList.add(pricing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        
        }

        return pricingList;
    }

    public boolean pricingExists(int typeID) {
        String sql = "SELECT 1 FROM Pricing WHERE TypeID = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, typeID);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.out.println(e);
        
        }
        return false;
    }

    public Pricing getPricingByTypeId(int id) {
        Pricing pricing = null;

        try {
            String strSQL = """
                            select * from pricing where TypeID = ?
                            """;
            stm = connection.prepareStatement(strSQL);
            stm.setInt(1, id);
            rs = stm.executeQuery();

            if (rs.next()) {
                return new Pricing(
                        rs.getInt("PricingID"),
                        rs.getInt("TypeID"),
                        rs.getBigDecimal("HourlyRate"),
                        rs.getBigDecimal("DailyRate")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        
        }

        return pricing;
    }

    public boolean addPricing(Pricing pricing) {
        if (pricingExists(pricing.getTypeID())) {
            return false;
        }

        try {
            String strSQL = """
                            insert into Pricing(TypeID, HourlyRate, DailyRate) VALUES(?, ?, ?);
                            """;
            stm = connection.prepareStatement(strSQL);
            stm.setInt(1, pricing.getTypeID());
            stm.setBigDecimal(2, pricing.getHourlyRate());
            stm.setBigDecimal(3, pricing.getDailyRate());

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        
        }

        return false;
    }

    public boolean updatePricing(Pricing pricing) {
        try {
            String strSQL = """
                            update Pricing set HourlyRate = ?, DailyRate = ? where TypeID = ?
                            """;
            stm = connection.prepareStatement(strSQL);
            stm.setBigDecimal(1, pricing.getHourlyRate());
            stm.setBigDecimal(2, pricing.getDailyRate());
            stm.setInt(3, pricing.getTypeID());

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        
        }

        return false;
    }

    public boolean deletePricing(Pricing pricing) {
        try {
            String strSQL = """
                            DELETE FROM Pricing WHERE PricingID = ?;
                            """;
            stm = connection.prepareStatement(strSQL);
            stm.setInt(1, pricing.getPricingID());

            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        
        }

        return false;
    }
}
