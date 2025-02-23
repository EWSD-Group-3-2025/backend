package org.teamSmurfs.backend.api.react.model;

public enum ReactEntityType {
    INVALID(0, "reactEntityType.invalid"),
    BLOG(2, "reactEntityType.blog"),
    EVENT(3, "reactEntityType.event"),
    CHAT(4, "reactEntityType.chat"),
    COMMENT(5, "reactEntityType.comment");

    private final Integer value;
    private final String code;

    private ReactEntityType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() { return value; }

    public String getCode() { return code; }

    public static ReactEntityType fromInt(final Integer value) {
        return switch (value) {
            case 0 -> INVALID;
            case 2 -> BLOG;
            case 3 -> EVENT;
            case 4 -> CHAT;
            case 5 -> COMMENT;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    public boolean isInvalid() {
        return this.value.equals(ReactEntityType.INVALID.getValue());
    }

    public boolean isBlog() {
        return this.value.equals(ReactEntityType.BLOG.getValue());
    }

    public boolean isEvent() {
        return this.value.equals(ReactEntityType.EVENT.getValue());
    }

    public boolean isChat() {
        return this.value.equals(ReactEntityType.CHAT.getValue());
    }

    public boolean isComment() {
        return this.value.equals(ReactEntityType.COMMENT.getValue());
    }
}
