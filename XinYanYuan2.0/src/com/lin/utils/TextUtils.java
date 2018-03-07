package com.lin.utils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

public class TextUtils
{
	private static boolean isEmpty(String s)
	{
		return s == null || s.equals("") || s.equals("null");
	}

	public static boolean equals(String a, String b)
	{
		return a.equals(b);
	}

	/**
	 * �ַ�������ת��
	 * 
	 * @param oldStr
	 * @return
	 */
	public static String ISO2UTF8(String oldStr)
	{
		String newStr = oldStr;
		System.out.println("old:" + oldStr);
		try
		{
			newStr = new String(oldStr.getBytes("ISO-8859-1"), "UTF-8");
			System.out.println("new:" + newStr);
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newStr;
	}

	public static String formatImageUrl(String imageUrl)
	{
		return DbUtil.HTTP_ADDRESS + imageUrl;
	}

	/**
	 * ��һ��String�������.�ж��Ƿ��д�������String�ǿյ�
	 * 
	 * @param strings
	 * @return
	 */
	public static boolean isStringsNulls(String... strings)
	{
		for (String string : strings)
		{
			if (TextUtils.isEmpty(string))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * ͨ��������ȷ������
	 * 
	 * @param mouth
	 * @param day
	 * @return
	 */
	public static String getStarSeat(long time)
	{
		String starSeat = null;
		// int mouth = Integer.parseInt(new SimpleDateFormat("MM")
		// .format(new Date(time)));
		// int day = Integer.parseInt(new SimpleDateFormat("dd").format(new
		// Date(
		// time)));
		Calendar instance = Calendar.getInstance();
		instance.setTime(new Date(time));
		int mouth = instance.get(Calendar.MONTH) + 1;
		int day = instance.get(Calendar.DAY_OF_MONTH);
		if ((mouth == 3 && day >= 21) || (mouth == 4 && day <= 19))
		{
			starSeat = "������";
		} else if ((mouth == 4 && day >= 20) || (mouth == 5 && day <= 20))
		{
			starSeat = "��ţ��";
		} else if ((mouth == 5 && day >= 21) || (mouth == 6 && day <= 21))
		{
			starSeat = "˫����";
		} else if ((mouth == 6 && day >= 22) || (mouth == 7 && day <= 22))
		{
			starSeat = "��з��";
		} else if ((mouth == 7 && day >= 23) || (mouth == 8 && day <= 22))
		{
			starSeat = "ʨ����";
		} else if ((mouth == 8 && day >= 23) || (mouth == 9 && day <= 22))
		{
			starSeat = "��Ů��";
		} else if ((mouth == 9 && day >= 23) || (mouth == 10 && day <= 23))
		{
			starSeat = "�����";
		} else if ((mouth == 10 && day >= 24) || (mouth == 11 && day <= 22))
		{
			starSeat = "��Ы��";
		} else if ((mouth == 11 && day >= 23) || (mouth == 12 && day <= 21))
		{
			starSeat = "������";
		} else if ((mouth == 12 && day >= 22) || (mouth == 1 && day <= 19))
		{
			starSeat = "Ħ����";
		} else if ((mouth == 1 && day >= 20) || (mouth == 2 && day <= 18))
		{
			starSeat = "ˮƿ��";
		} else
		{
			starSeat = "˫����";
		}
		return starSeat;
	}

	/**
	 * ͨ��������ȷ������
	 * 
	 * @param time
	 * @return
	 */
	public static int getAge(long time)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int currentYear = c.get(Calendar.YEAR);
		c.setTimeInMillis(time);
		int oldYear = c.get(Calendar.YEAR);
		return currentYear - oldYear;
	}
}
