/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class Ticket {

    private int ticketID;
    private String ticketCode;
    private String licensePlate;
    private int typeID;
    private int slotID;
    private Integer customerID;
    private LocalDateTime entryTime;
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    private String status;
    private int createdBy;

    /* Object references for JOIN queries */
    private VehicleType vehicleType;
    private Slot slot;
    private User customer;
    private User createdByStaff;
    private Transaction transaction;

    public Ticket() {
    }

    public Ticket(int ticketID, String ticketCode, String licensePlate, int typeID, int slotID, Integer customerID, LocalDateTime entryTime, BigDecimal hourlyRate, BigDecimal dailyRate, String status, int createdBy) {
        this.ticketID = ticketID;
        this.ticketCode = ticketCode;
        this.licensePlate = licensePlate;
        this.typeID = typeID;
        this.slotID = slotID;
        this.customerID = customerID;
        this.entryTime = entryTime;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
        this.status = status;
        this.createdBy = createdBy;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
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

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public String getEntryTimeFormatted() {
        if (entryTime == null) return "-";
        return entryTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"));
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getCreatedByStaff() {
        return createdByStaff;
    }

    public void setCreatedByStaff(User createdByStaff) {
        this.createdByStaff = createdByStaff;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
