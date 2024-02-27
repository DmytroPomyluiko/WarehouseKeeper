package com.warehousekeeper.root.util;

public class CustomerNotCreatedException extends RuntimeException{
    public CustomerNotCreatedException(String msg) {
        super(msg);
    }
}
