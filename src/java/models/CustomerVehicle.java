/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */
public class CustomerVehicle {

    private int vehicleID;
    private int userID;
    private String licensePlate;
    private int typeID;
    private VehicleType vehicleType;

    public CustomerVehicle() {
    }

    public CustomerVehicle(int vehicleID, int userID, String licensePlate, VehicleType vehicleType) {
        this.vehicleID = vehicleID;
        this.userID = userID;
        this.licensePlate = licensePlate;
        this.typeID = typeID;
        this.vehicleType = vehicleType;
    }

    public CustomerVehicle(int userID, String licensePlate,int typeID, VehicleType vehicleType) {
        this.vehicleID = vehicleID;
        this.userID = userID;
        this.licensePlate = licensePlate;
        this.typeID = typeID;
        this.vehicleType = vehicleType;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

}
