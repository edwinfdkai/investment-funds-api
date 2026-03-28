package com.test.bank.model.enums;

public enum TransactionType {

    OPENING,
    CANCELLATION;

    public String getValue() {
        return name();
    }
}
