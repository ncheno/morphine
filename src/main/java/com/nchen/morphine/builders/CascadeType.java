package com.nchen.morphine.builders;

public enum CascadeType {
    ON_UPDATE_CASCADE("ON UPDATE CASCADE"), ON_DELETE_CASCADE("ON DELETE CASCADE"),
    CASCADE_ALL("ON UPDATE CASCADE ON DELETE CASCADE");

    private String action;

    CascadeType(String action) {
        this.action = action;
    }

    public String getValue() {
        return action;
    }
}
