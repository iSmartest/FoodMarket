package com.lixin.foodmarket.bean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/24
 * My mailbox is 1403241630@qq.com
 */

public class ClassListBean {
    public String result;
    public String resultNote;
    public int totalPage;
    public List<commoditys> commoditys;
    public class commoditys{
        public String commodityid;
        public String commodityTitle;
        public String commodityIcon;
        public String commodityOriginalPrice;
        public String commodityNewPrice;
        public String commoditySeller;
    }
}
