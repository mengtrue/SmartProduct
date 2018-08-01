package com.gtk.smartmanagement.data;

import java.util.HashMap;
import java.util.Map;

public class LoginData {
    public static final String username = "username";
    public static final String password = "password";

    public class LoginJsonBean {
        public String status;
        public String msg;

        @Override
        public String toString() {
            return msg;
        }
    }

    public static Map<String, String> generatePostData(String name, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put(username, name);
        map.put(password, pwd);
        return map;
    }
}
