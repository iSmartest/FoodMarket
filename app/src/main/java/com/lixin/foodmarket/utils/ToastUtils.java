package com.lixin.foodmarket.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 小火
 * Create time on  2017/3/22
 * My mailbox is 1403241630@qq.com
 */

public class ToastUtils
{
	/**
	 * 使用Toast显示信息，时间较短
	 * @param context
	 * @param strId
	 */
	public static void showMessageShort(Context context, int strId)
	{
		Toast.makeText(context, strId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 使用Toast显示信息，时间较短
	 * @param context
	 * @param strId
	 */
	public static void showMessageShort(Context context, CharSequence text)
	{
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 使用Toast显示信息，时间较长
	 * @param context
	 * @param strId
	 */
	public static void showMessageLong(Context context, int strId)
	{
		Toast.makeText(context, strId, Toast.LENGTH_LONG).show();
	}

	/**
	 * 使用Toast显示信息，时间较长
	 * @param context
	 * @param strId
	 */
	public static void showMessageLong(Context context, CharSequence text)
	{
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
}
