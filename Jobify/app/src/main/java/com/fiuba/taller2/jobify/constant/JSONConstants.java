package com.fiuba.taller2.jobify.constant;

public class JSONConstants {

    public final static String ID = "id";
    public final static String TOKEN = "token";
    public static final String ERROR_MESSAGE = "error";

    public static class Arrays {
        public final static String CONTANCTS = "contacts";
        public final static String USERS = "users";
        public static final String SKILLS = "skills";
    }

    public static class User {
        public final static String USER = "user";
        public final static String FIRST_NAME = "first_name";
        public final static String LAST_NAME = "last_name";
        public final static String ABOUT = "about";
        public final static String PROFILE_PIC_URL = "profile_pic";
        public static final String CONTACTS = "contacts";
    }

    public static class Chat {
        public static final String CHATS = "chats";
        public final static String CONTACT = "contact";
        public final static String LAST_MESSAGE = "last_message";
        public final static String MESSAGES = "messages";
    }

    public class Contact {
        public final static String FIRST_NAME = "first_name";
        public final static String LAST_NAME = "last_name";
        public static final String PROFILE_PIC = "profile_pic";
        public static final String USER_ID = "user_id";
    }
}
