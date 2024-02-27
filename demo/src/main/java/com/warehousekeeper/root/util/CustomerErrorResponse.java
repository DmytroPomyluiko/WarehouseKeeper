package com.warehousekeeper.root.util;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
public class CustomerErrorResponse {
    private String message;
    private Date date;

    public CustomerErrorResponse(String message, Date date) {
        this.message = message;
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = (Date) date.clone();
    }
}
