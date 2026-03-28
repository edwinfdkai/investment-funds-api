package com.test.bank.model.enums;

public enum NotificationPreference {

    EMAIL,
    SMS;

    public String getValue() {
        return name();
    }

    public static NotificationPreference fromStoredValue(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("notification preference is required");
        }
        return valueOf(raw.trim().toUpperCase());
    }
}
