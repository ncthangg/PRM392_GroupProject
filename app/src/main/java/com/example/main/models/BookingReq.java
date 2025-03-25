package com.example.main.models;

public class BookingReq {
    private String CustomerId;
    private String ServiceId;
    private String WorkingDate;
    private String Address;
    private String WorkingTime;
    private String Note;

    public BookingReq(String customerId, String serviceId, String workingDate, String workingTime, String address, String note) {
        this.CustomerId = customerId;
        this.ServiceId = serviceId;
        this.WorkingDate = workingDate;
        this.Address = address;
        this.WorkingTime = workingTime;
        this.Note = note;
    }
}
