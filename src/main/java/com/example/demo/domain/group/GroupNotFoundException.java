package com.example.demo.domain.group;

public class GroupNotFoundException extends RuntimeException {
    private int status = 404;
    public GroupNotFoundException(String message) {
        super(message);
    }

    public int getStatus() {
        return status;
    }
}
