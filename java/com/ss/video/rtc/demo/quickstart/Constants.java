package com.ss.video.rtc.demo.quickstart;

/**
 * VolcEngineRTC 常量定义
 */
public class Constants {

    //APPID 使用SDK前需要为自己的应用申请一个AppId，详情参见{https://www.volcengine.com/docs/6348/69865}
    public static final String APPID = "630d5b3378d969018cd522a4";

    //TOKEN 加入房间的时候需要使用token完成鉴权，详情参见{https://www.volcengine.com/docs/6348/70121}
    public static final String TOKEN = "001630d5b3378d969018cd522a4PQDmFMgCdAAaY/Q6I2MGADExNDUxNAEAWQYAAAD0OiNjAQD0OiNjAgD0OiNjAwD0OiNjBAD0OiNjBQD0OiNjIADPhg4sBgE7la3blMpY9RicS9YqEu+AcGWMpsSPWsPxMg==";
    //INPUT_REGEX SDK 对房间名、用户名的限制是：非空且最大长度不超过128位的数字、大小写字母、@ . _ | -
    public static final String INPUT_REGEX = "^[a-zA-Z0-9@._|-]{1,128}$";

    public static final String ROOM_ID_EXTRA = "extra_room_id";

    public static final String USER_ID_EXTRA = "extra_user_id";
}
