package org.teamSmurfs.backend.features.library.model;

public enum BookCategory {
    INVALID(0, "Invalid"),
    COMPUTER_SCIENCE(1, "Computer Science"),
    PROGRAMMING(2, "Programming"),
    PROJECT_MANAGEMENT(3, "Project Management"),
    DATABASE(4, "Database"),
    OPERATION_SYSTEM(5, "Operating System");

    private final Integer value;
    private final String code;

    private BookCategory(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() { return value; }

    public String getCode() { return code; }

    public static BookCategory fromInt(final Integer value) {
        return switch (value) {
            case 0 -> INVALID;
            case 1 -> COMPUTER_SCIENCE;
            case 2 -> PROGRAMMING;
            case 3 -> PROJECT_MANAGEMENT;
            case 4 -> DATABASE;
            case 5 -> OPERATION_SYSTEM;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    public boolean isInvalid() {
        return this.value.equals(BookCategory.INVALID.getValue());
    }

    public boolean isProgramming() { return value.equals(BookCategory.PROGRAMMING.getValue()); }

    public boolean isProjectManagement() { return value.equals(BookCategory.PROJECT_MANAGEMENT.getValue()); }

    public boolean isDatabase() { return value.equals(BookCategory.DATABASE.getValue()); }

    public boolean isOperationSystem() { return value.equals(BookCategory.OPERATION_SYSTEM.getValue()); }
}
