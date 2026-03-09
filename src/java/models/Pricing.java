/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
public class Pricing {
    private int pricingID;
    private int typeID;
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    private VehicleType vehicleType;

    public Pricing() {
    }

    public Pricing(int pricingID, int typeID, BigDecimal hourlyRate, BigDecimal dailyRate) {
        this.pricingID = pricingID;
        this.typeID = typeID;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
    }
    
    public Pricing(int typeID, BigDecimal hourlyRate, BigDecimal dailyRate) {
        this.typeID = typeID;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
    }

    public int getPricingID() {
        return pricingID;
    }

    public void setPricingID(int pricingID) {
        this.pricingID = pricingID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    
}
