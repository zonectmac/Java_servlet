package com.lin.model;

import java.util.List;

/**
 * ���۶���
 * 
 * @author ����
 * 
 */
public class Comments
{
	private String commentId;// ���۵�ID
	private String nickNameC;// ���۵��û��ǳ�
	private String userPhoneC;// ���۵��û��˺�
	private String iconC;// ���۵��û�ͷ��
	private String timeC;// ���۵�ʱ��
	private String contentC;// ���۵�����
	private List<Reply> replys;// �ظ�

	public String getCommentId()
	{
		return commentId;
	}

	public void setCommentId(String commentId)
	{
		this.commentId = commentId;
	}

	public String getNickNameC()
	{
		return nickNameC;
	}

	public void setNickNameC(String nickNameC)
	{
		this.nickNameC = nickNameC;
	}

	public String getUserPhoneC()
	{
		return userPhoneC;
	}

	public void setUserPhoneC(String userPhoneC)
	{
		this.userPhoneC = userPhoneC;
	}

	public String getIconC()
	{
		return iconC;
	}

	public void setIconC(String iconC)
	{
		this.iconC = iconC;
	}

	public String getTimeC()
	{
		return timeC;
	}

	public void setTimeC(String timeC)
	{
		this.timeC = timeC;
	}

	public String getContentC()
	{
		return contentC;
	}

	public void setContentC(String contentC)
	{
		this.contentC = contentC;
	}

	public List<Reply> getReplys()
	{
		return replys;
	}

	public void setReplys(List<Reply> replys)
	{
		this.replys = replys;
	}

	public Comments(String commentId, String nickNameC, String userPhoneC, String iconC, String timeC, String contentC,
			List<Reply> replys)
	{
		super();
		this.commentId = commentId;
		this.nickNameC = nickNameC;
		this.userPhoneC = userPhoneC;
		this.iconC = iconC;
		this.timeC = timeC;
		this.contentC = contentC;
		this.replys = replys;
	}

	@Override
	public String toString()
	{
		return "Comments [commentId=" + commentId + ", nickNameC=" + nickNameC + ", userPhoneC=" + userPhoneC
				+ ", iconC=" + iconC + ", timeC=" + timeC + ", contentC=" + contentC + ", replys=" + replys + "]";
	}

	public Comments()
	{
		// TODO Auto-generated constructor stub
	}
}
