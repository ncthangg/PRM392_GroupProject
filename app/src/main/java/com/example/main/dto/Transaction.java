package com.example.main.dto;

public class Transaction {
    private String title;
    private String date;
    private String dateTime;
    private String amount;

    public Transaction(String title, String date, String dateTime, String amount) {
        this.title = title;
        this.date = date;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getAmount() {
        return amount;
    }
}
