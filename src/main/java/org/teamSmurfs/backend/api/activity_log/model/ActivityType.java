package org.teamSmurfs.backend.api.activity_log.model;

public enum ActivityType {
    INVALID(0, "activity.invalid"),

    // 🔐 Authentication
    LOGIN(1, "activity.login"),
    LOGOUT(2, "activity.logout"),

    // 📝 Blog Interactions
    CREATE_BLOG(3, "activity.createBlog"),
    COMMENT_BLOG(4, "activity.commentBlog"),
    REACT_BLOG(5, "activity.reactBlog"),

    // 💬 Chat System
    SEND_CHAT_MESSAGE(6, "activity.sendChatMessage"),
    CREATE_CHAT_ROOM(7, "activity.createChatRoom"),

    // 📂 Media Management
    UPLOAD_MEDIA(8, "activity.uploadMedia"),

    // 📅 Events
    CREATE_EVENT(9, "activity.createEvent"),
    ATTEND_EVENT(10, "activity.attendEvent"),

    // 📅 Meetings
    CREATE_MEETING(11, "activity.createMeeting"),
    JOIN_MEETING(12, "activity.joinMeeting"),

    // 🏫 Tutor-Student Relationship
    CREATE_ALLOCATION(13, "activity.createAllocation"),

    // 📚 Course Enrollment
    ENROLL_COURSE(14, "activity.enrollCourse"),
    DROP_COURSE(15, "activity.dropCourse"),

    // 🎓 Specializations
    ADD_SPECIALIZATION(16, "activity.addSpecialization"),

    // 👥 User Management (CRUD)
    CREATE_USER(19, "activity.createUser"),
    UPDATE_USER(20, "activity.updateUser"),
    DELETE_USER(21, "activity.deleteUser"),

    // 📚 Course Management (CRUD)
    CREATE_COURSE(22, "activity.createCourse"),
    UPDATE_COURSE(23, "activity.updateCourse"),
    DELETE_COURSE(24, "activity.deleteCourse"),

    // 🎓 Specialization Management (CRUD)
    CREATE_SPECIALIZATION(25, "activity.createSpecialization"),
    UPDATE_SPECIALIZATION(26, "activity.updateSpecialization"),
    DELETE_SPECIALIZATION(27, "activity.deleteSpecialization"),

    // 🏢 Department Management (CRUD)
    CREATE_DEPARTMENT(28, "activity.createDepartment"),
    UPDATE_DEPARTMENT(29, "activity.updateDepartment"),
    DELETE_DEPARTMENT(30, "activity.deleteDepartment");

    private final Integer value;
    private final String code;

    private ActivityType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() { return value; }

    public String getCode() { return code; }

    public static ActivityType fromInt(final Integer value) {
        return switch (value) {
            case 0 -> INVALID;
            case 1 -> LOGIN;
            case 2 -> LOGOUT;
            case 3 -> CREATE_BLOG;
            case 4 -> COMMENT_BLOG;
            case 5 -> REACT_BLOG;
            case 6 -> SEND_CHAT_MESSAGE;
            case 7 -> CREATE_CHAT_ROOM;
            case 8 -> UPLOAD_MEDIA;
            case 9 -> CREATE_EVENT;
            case 10 -> ATTEND_EVENT;
            case 11 -> CREATE_MEETING;
            case 12 -> JOIN_MEETING;
            case 13 -> CREATE_ALLOCATION;
            case 14 -> ENROLL_COURSE;
            case 15 -> DROP_COURSE;
            case 16 -> ADD_SPECIALIZATION;
            case 19 -> CREATE_USER;
            case 20 -> UPDATE_USER;
            case 21 -> DELETE_USER;
            case 22 -> CREATE_COURSE;
            case 23 -> UPDATE_COURSE;
            case 24 -> DELETE_COURSE;
            case 25 -> CREATE_SPECIALIZATION;
            case 26 -> UPDATE_SPECIALIZATION;
            case 27 -> DELETE_SPECIALIZATION;
            case 28 -> CREATE_DEPARTMENT;
            case 29 -> UPDATE_DEPARTMENT;
            case 30 -> DELETE_DEPARTMENT;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    public boolean isInvalid() {
        return this.value.equals(ActivityType.INVALID.getValue());
    }
}
