package com.lixin.foodmarket.budiler;


import com.lixin.foodmarket.budiler.budiler.GetBuilder;
import com.lixin.foodmarket.budiler.budiler.OtherRequest;
import com.lixin.foodmarket.budiler.budiler.RequestCall;
import com.lixin.foodmarket.utils.OkHttpUtils;

/**
 * Created by 小火
 * Create time on  2017/3/22
 * My mailbox is 1403241630@qq.com
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
