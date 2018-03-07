package com.lin.model;

/**
 * 点赞对象
 * 
 * @author 华明
 * 
 */
public class Praises
{
	private int praiseId;// 点赞ID
	String nickNameP;// 点赞的用户昵称
	String userPhoneP;// 点赞的用户账号

	public Praises()
	{
	}

	public Praises(String praiseId, String nickNameP, String userPhoneP)
	{
		this(Integer.parseInt(praiseId), nickNameP, userPhoneP);
	}

	public Praises(int praiseId, String nickNameP, String userPhoneP)
	{
		this.praiseId = praiseId;
		this.nickNameP = nickNameP;
		this.userPhoneP = userPhoneP;
	}

	@Override
	public String toString()
	{
		return "Praises [praiseId=" + praiseId + ", nickNameP=" + nickNameP + ", userPhoneP=" + userPhoneP + "]";
	}

	public int getPraiseId()
	{
		return praiseId;
	}

	public void setPraiseId(int praiseId)
	{
		this.praiseId = praiseId;
	}

	public String getNickNameP()
	{
		return nickNameP;
	}

	public void setNickNameP(String nickNameP)
	{
		this.nickNameP = nickNameP;
	}

	public String getUserPhoneP()
	{
		return userPhoneP;
	}

	public void setUserPhoneP(String userPhoneP)
	{
		this.userPhoneP = userPhoneP;
	}

}
