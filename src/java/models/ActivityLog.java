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
    public String actionType; // "Check-In" hoặc "Check-Out"
    public String staffName;

    public ActivityLog() {
    }

    public LocalDateTime getActivityTime() {
        return activityTime;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getActionType() {
        return actionType;
    }

    public String getStaffName() {
        return staffName;
    }
    
    public String getFormattedTime() {
        if (activityTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return activityTime.format(formatter);
        }
        return "";
    }
}
