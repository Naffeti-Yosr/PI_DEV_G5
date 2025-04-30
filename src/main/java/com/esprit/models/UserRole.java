package com.esprit.models;

public enum UserRole {
    ADMIN("ADMIN"),
    BLOGER_ADMIN("BLOGER_ADMIN"),
    CLIENT("CLIENT"),
    EVENT_PLANNER("EVENT_PLANNER"),
    PRODUCT_OWNER("PRODUCT_OWNER"),
    TRUCK_DRIVER("TRUCK_DRIVER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserRole fromString(String roleStr) {
        for (UserRole role : UserRole.values()) {
            if (role.getValue().equalsIgnoreCase(roleStr)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + roleStr);
    }
}
