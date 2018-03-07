package com.lin.model;

/**
 * 回复对象
 * 
 * @author 华明
 * 
 */
public class Reply
{

	private String nickNameR;// 回复者的用户昵称
	private String userPhoneR;// 回复者的用户账号
	private String toNickNameR;// 被回复者的用户昵称
	private String toUserPhoneR;// 被回复者的用户账号
	private String contentR;// 回复的内容
	private String timeR;// 回复的时间
	private String replyId;// 回复的ID

	public Reply()
	{
	}

	public Reply(String nickNameR, String userPhoneR, String toNickNameR, String toUserPhoneR, String contentR,
			String timeR, String replyId)
	{
		super();
		this.nickNameR = nickNameR;
		this.userPhoneR = userPhoneR;
		this.toNickNameR = toNickNameR;
		this.toUserPhoneR = toUserPhoneR;
		this.contentR = contentR;
		this.timeR = timeR;
		this.replyId = replyId;
	}

	@Override
	public String toString()
	{
		return "Reply [nickNameR=" + nickNameR + ", userPhoneR=" + userPhoneR + ", toNickNameR=" + toNickNameR
				+ ", toUserPhoneR=" + toUserPhoneR + ", contentR=" + contentR + ", timeR=" + timeR + ", replyId="
				+ replyId + "]";
	}

	public String getNickNameR()
	{
		return nickNameR;
	}

	public void setNickNameR(String nickNameR)
	{
		this.nickNameR = nickNameR;
	}

	public String getUserPhoneR()
	{
		return userPhoneR;
	}

	public void setUserPhoneR(String userPhoneR)
	{
		this.userPhoneR = userPhoneR;
	}

	public String getToNickNameR()
	{
		return toNickNameR;
	}

	public void setToNickNameR(String toNickNameR)
	{
		this.toNickNameR = toNickNameR;
	}

	public String getToUserPhoneR()
	{
		return toUserPhoneR;
	}

	public void setToUserPhoneR(String toUserPhoneR)
	{
		this.toUserPhoneR = toUserPhoneR;
	}

	public String getContentR()
	{
		return contentR;
	}

	public void setContentR(String contentR)
	{
		this.contentR = contentR;
	}

	public String getTimeR()
	{
		return timeR;
	}

	public void setTimeR(String timeR)
	{
		this.timeR = timeR;
	}

	public String getReplyId()
	{
		return replyId;
	}

	public void setReplyId(String replyId)
	{
		this.replyId = replyId;
	}
}
