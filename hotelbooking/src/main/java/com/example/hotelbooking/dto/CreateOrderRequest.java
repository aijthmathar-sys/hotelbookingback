package com.example.hotelbooking.dto;

import java.time.LocalDate;

public class CreateOrderRequest {

    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int guests;
    private double amount;

    public CreateOrderRequest() {}

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount= amount;
    }
}