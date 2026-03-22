/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author tuant
 */
public class ActivityLog {
    public LocalDateTime activityTime;
    public String licensePlate;
    public String slot;
    public String zone;
    public String vehicleType;
    public String actionType; // "Check-In" hoac "Check-Out"
    public String staffName;
    public java.math.BigDecimal amount;

    public ActivityLog() {
    }

    public LocalDateTime getActivityTime() {
        return activityTime;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getSlot() {
        return slot;
    }

    public String getZone() {
        return zone;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getActionType() {
        return actionType;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getFormattedTime() {
        if (activityTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/M/yyyy");
            return activityTime.format(formatter);
        }
        return "";
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        if (amount != null && amount.compareTo(java.math.BigDecimal.ZERO) > 0) {
            return utils.FormatCurrency.formatVND(amount);
        }
        if ("Check-In".equals(actionType)) {
            return "";
        }
        return "0 ₫";
    }
}
