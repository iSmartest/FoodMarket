package com.lixin.foodmarket.bean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/26
 * My mailbox is 1403241630@qq.com
 */

public class MyAddressBean {
    public String result;
    public String resultNote;
    public List<addressList> addressList;

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

    public List<MyAddressBean.addressList> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<MyAddressBean.addressList> addressList) {
        this.addressList = addressList;
    }

    public class addressList{
        public String receiverid;//收货地址id,
        public String receiverSelected;//是否为默认，1为默认，0为非默认
        public String receiverName;//收货人姓名
        public String receiverPhone;//收货人电话
        public String receiverAddress;//收货人省市
        public String receiverDetailAddress;//收货人人街道
        public String receiverBuildingNum;//收货人楼栋
        public String receiverPostcode;//收货人邮编
        public String receiverCell;//收货人小区
        public String receiverElement;//收货人单元

        public String getReceiverid() {
            return receiverid;
        }

        public void setReceiverid(String receiverid) {
            this.receiverid = receiverid;
        }

        public String getReceiverSelected() {
            return receiverSelected;
        }

        public void setReceiverSelected(String receiverSelected) {
            this.receiverSelected = receiverSelected;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getReceiverPhone() {
            return receiverPhone;
        }

        public void setReceiverPhone(String receiverPhone) {
            this.receiverPhone = receiverPhone;
        }

        public String getReceiverAddress() {
            return receiverAddress;
        }

        public void setReceiverAddress(String receiverAddress) {
            this.receiverAddress = receiverAddress;
        }

        public String getReceiverDetailAddress() {
            return receiverDetailAddress;
        }

        public void setReceiverDetailAddress(String receiverDetailAddress) {
            this.receiverDetailAddress = receiverDetailAddress;
        }

        public String getReceiverBuildingNum() {
            return receiverBuildingNum;
        }

        public void setReceiverBuildingNum(String receiverBuildingNum) {
            this.receiverBuildingNum = receiverBuildingNum;
        }

        public String getReceiverPostcode() {
            return receiverPostcode;
        }

        public void setReceiverPostcode(String receiverPostcode) {
            this.receiverPostcode = receiverPostcode;
        }

        public String getReceiverCell() {
            return receiverCell;
        }

        public void setReceiverCell(String receiverCell) {
            this.receiverCell = receiverCell;
        }

        public String getReceiverElement() {
            return receiverElement;
        }

        public void setReceiverElement(String receiverElement) {
            this.receiverElement = receiverElement;
        }
    }
}
