package com.lixin.foodmarket.bean;

/**
 * Created by 小火
 * Create time on  2017/5/22
 * My mailbox is 1403241630@qq.com
 */

public class UserRegisterBean {
    public String result;//请求结果 0成功 1失败
    public String resultNote;//失败原因
    public String uid;//用户ID

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultNote() {
        return resultNote;
    }

    public void setResultNote(String resultNote) {
        this.resultNote = resultNote;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
