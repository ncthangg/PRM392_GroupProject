package com.example.main.models;

import java.util.Date;

public class BookingItem {
    public String Id;
    public String CustomerId;
    public String MechanistId;
    public String ServiceId;
    public Service Service;
    public Rating Rating;
    public String WorkingDate;
    public String Address;
    public String WorkingTime;
    public String Status;
    public Date BookingDate;
    public String Note;

    public BookingItem(String id, String customerId, String mechanistId, String serviceId, Service service, String workingDate, String address, String workingTime, String status, Date bookingDate, String note) {
        Id = id;
        CustomerId = customerId;
        MechanistId = mechanistId;
        ServiceId = serviceId;
        Service = service;
        WorkingDate = workingDate;
        Address = address;
        WorkingTime = workingTime;
        Status = status;
        BookingDate = bookingDate;
        Note = note;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getMechanistId() {
        return MechanistId;
    }

    public void setMechanistId(String mechanistId) {
        MechanistId = mechanistId;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public Service getService() {
        return Service;
    }

    public void setService(Service service) {
        Service = service;
    }

    public String getWorkingDate() {
        return WorkingDate;
    }

    public void setWorkingDate(String workingDate) {
        WorkingDate = workingDate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getWorkingTime() {
        return WorkingTime;
    }

    public void setWorkingTime(String workingTime) {
        WorkingTime = workingTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Date getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        BookingDate = bookingDate;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}

