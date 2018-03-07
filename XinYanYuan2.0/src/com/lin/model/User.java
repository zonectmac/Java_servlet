package com.lin.model;

public class User
{
	private String userId;
	private String nickName;
	private String icon;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId + "";
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	@Override
	public String toString()
	{
		return "User [userId=" + userId + ", nickName=" + nickName + ", icon=" + icon + "]";
	}

	public User(int userId, String nickName, String icon)
	{
		setUserId(userId);
		this.nickName = nickName;
		this.icon = icon;
	}

	public User()
	{
	}

}
