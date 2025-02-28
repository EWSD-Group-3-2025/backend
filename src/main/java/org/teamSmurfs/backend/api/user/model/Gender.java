package org.teamSmurfs.backend.api.user.model;

public enum Gender {
    INVALID(0, "gender.invalid"),
    MALE(1, "gender.male"),
    FEMALE(2, "gender.female"),
    OTHER(3, "gender.other");

    private final Integer value;
    private final String code;

    private Gender(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() { return value; }

    public String getCode() { return code; }

    public static Gender fromInt(final Integer value) {
        return switch (value) {
            case 0 -> INVALID;
            case 1 -> MALE;
            case 2 -> FEMALE;
            case 3 -> OTHER;
            default -> throw new IllegalArgumentException("Invalid gender value provided.");
        };
    }

    public boolean isInvalid() { return this.value.equals(Gender.INVALID.getValue()); }

    public boolean isMale() { return this.value.equals(Gender.MALE.getValue()); }

    public boolean isFemale() { return this.value.equals(Gender.FEMALE.getValue()); }

    public boolean isOther() { return this.value.equals(Gender.OTHER.getValue()); }
}
