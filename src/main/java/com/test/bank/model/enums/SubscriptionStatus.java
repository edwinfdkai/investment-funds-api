package com.test.bank.model.enums;

public enum SubscriptionStatus {

    ACTIVE,
    CANCELLED;

    public String getValue() {
        return name();
    }
}
