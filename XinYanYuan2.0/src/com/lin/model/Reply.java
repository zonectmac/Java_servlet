package com.lin.model;

/**
 * �ظ�����
 * 
 * @author ����
 * 
 */
public class Reply
{

	private String nickNameR;// �ظ��ߵ��û��ǳ�
	private String userPhoneR;// �ظ��ߵ��û��˺�
	private String toNickNameR;// ���ظ��ߵ��û��ǳ�
	private String toUserPhoneR;// ���ظ��ߵ��û��˺�
	private String contentR;// �ظ�������
	private String timeR;// �ظ���ʱ��
	private String replyId;// �ظ���ID

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
