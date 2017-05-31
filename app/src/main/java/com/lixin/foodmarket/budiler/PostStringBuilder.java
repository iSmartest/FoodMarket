package com.lixin.foodmarket.budiler;
import com.lixin.foodmarket.budiler.budiler.OkHttpRequestBuilder;
import com.lixin.foodmarket.budiler.budiler.PostStringRequest;
import com.lixin.foodmarket.budiler.budiler.RequestCall;

import okhttp3.MediaType;

/**
 * Created by 小火
 * Create time on  2017/3/22
 * My mailbox is 1403241630@qq.com
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder>
{
    private String content;
    private MediaType mediaType;


    public PostStringBuilder content(String content)
    {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType)
    {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build()
    {
        return new PostStringRequest(url, tag, params, headers, content, mediaType,id).build();
    }


}
