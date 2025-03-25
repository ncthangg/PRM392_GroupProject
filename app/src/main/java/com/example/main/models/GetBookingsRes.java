package com.example.main.models;

import java.util.List;

public class GetBookingsRes {
    public DataWrapper data;

    public static class DataWrapper {
        public List<BookingItem> Data;
    }
}
