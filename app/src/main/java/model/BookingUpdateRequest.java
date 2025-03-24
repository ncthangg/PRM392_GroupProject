package model;

import com.google.gson.annotations.SerializedName;

public class BookingUpdateRequest {
    @SerializedName("Status")
    private String status;

    public BookingUpdateRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
