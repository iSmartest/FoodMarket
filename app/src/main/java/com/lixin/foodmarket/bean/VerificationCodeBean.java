package com.lixin.foodmarket.bean;

/**
 * Created by 小火
 * Create time on  2017/3/22
 * My mailbox is 1403241630@qq.com
 * 验证码实体类
 */

public class VerificationCodeBean {
    public String reason;
    public int error_code;


    public class resultBean {
        public String sid;
        public int fee;
        public int count;
    }
}
