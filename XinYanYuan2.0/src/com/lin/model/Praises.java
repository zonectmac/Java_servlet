package com.lin.model;

/**
 * ���޶���
 * 
 * @author ����
 * 
 */
public class Praises
{
	private int praiseId;// ����ID
	String nickNameP;// ���޵��û��ǳ�
	String userPhoneP;// ���޵��û��˺�

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
