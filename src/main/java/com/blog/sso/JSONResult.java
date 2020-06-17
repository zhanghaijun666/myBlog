package com.blog.sso;

import org.json.JSONObject;

public class JSONResult {
    public static String fillResultString(Integer code, String message, Object result) {
        JSONObject jsonObject = new JSONObject() {{
            put("code", code);
            put("msg", message);
            put("result", result);
        }};

        return jsonObject.toString();
    }
}
