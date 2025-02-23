package org.teamSmurfs.backend.api.media.model;

public enum MediaType {
    INVALID(0, "mediaType.invalid"),
    PROFILE_PHOTO(1, "mediaType.profilePhoto"),
    BLOG(2, "mediaType.blog"),
    EVENT(3, "mediaType.event"),
    CHAT(4, "mediaType.chat"),
    COMMENT(5, "mediaType.comment");

    private final Integer value;
    private final String code;

    private MediaType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() { return value; }

    public String getCode() { return code; }

    public static MediaType fromInt(final Integer value) {
        return switch (value) {
            case 0 -> INVALID;
            case 1 -> PROFILE_PHOTO;
            case 2 -> BLOG;
            case 3 -> EVENT;
            case 4 -> CHAT;
            case 5 -> COMMENT;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    public boolean isInvalid() {
        return this.value.equals(MediaType.INVALID.getValue());
    }

    public boolean isProfilePhoto() {
        return this.value.equals(MediaType.PROFILE_PHOTO.getValue());
    }

    public boolean isBlog() {
        return this.value.equals(MediaType.BLOG.getValue());
    }

    public boolean isEvent() {
        return this.value.equals(MediaType.EVENT.getValue());
    }

    public boolean isChat() {
        return this.value.equals(MediaType.CHAT.getValue());
    }

    public boolean isComment() {
        return this.value.equals(MediaType.COMMENT.getValue());
    }
}
