package com.lin.model;

import java.util.List;

/**
 * 评论对象
 * 
 * @author 华明
 * 
 */
public class Comments
{
	private String commentId;// 评论的ID
	private String nickNameC;// 评论的用户昵称
	private String userPhoneC;// 评论的用户账号
	private String iconC;// 评论的用户头像
	private String timeC;// 评论的时间
	private String contentC;// 评论的内容
	private List<Reply> replys;// 回复

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
