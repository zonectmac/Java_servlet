package com.lin.model;

import java.util.List;

import com.lin.utils.DbUtil;

public class Speak
{
	private int speakId;// 说说的ID
	private String icon;// 头像URl
	private String nickName;// 昵称
	private String publicTime;// 发布时间
	private String content;// 内容
	private int browseNum;// browseNum
	private String mobileType;// 手机机型
	private List<String> photos;// 说说携带的图片，有多张
	private List<Praises> praises;// 说说携带的图片，有多张
	private List<Comments> comments;// 评论

	public int getSpeakId()
	{
		return speakId;
	}

	public void setSpeakId(int speakId)
	{
		this.speakId = speakId;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public String getPublicTime()
	{
		return publicTime;
	}

	public void setPublicTime(String publicTime)
	{
		this.publicTime = publicTime;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public int getBrowseNum()
	{
		return browseNum;
	}

	public void setBrowseNum(int browseNum)
	{
		this.browseNum = browseNum;
	}

	public String getMobileType()
	{
		return mobileType;
	}

	public void setMobileType(String mobileType)
	{
		this.mobileType = mobileType;
	}

	public List<String> getPhotos()
	{
		return photos;
	}

	public void setPhotos(List<String> photos)
	{
		this.photos = photos;
	}

	public List<Praises> getPraises()
	{
		return praises;
	}

	public void setPraises(List<Praises> praises)
	{
		this.praises = praises;
	}

	public List<Comments> getComments()
	{
		return comments;
	}

	public void setComments(List<Comments> comments)
	{
		this.comments = comments;
	}

	@Override
	public String toString()
	{
		return "Speak [speakId=" + speakId + ", icon=" + icon + ", nickName=" + nickName + ", publicTime=" + publicTime
				+ ", content=" + content + ", browseNum=" + browseNum + ", mobileType=" + mobileType + ", photos="
				+ photos + ", praises=" + praises + ", comments=" + comments + "]";
	}

	public Speak(int speakId, String icon, String nickName, String publicTime, String content, int browseNum,
			String mobileType, List<String> photos, List<Praises> praises, List<Comments> comments)
	{
		super();
		this.speakId = speakId;
		this.icon = DbUtil.HTTP_ADDRESS+icon;
		this.nickName = nickName;
		this.publicTime = publicTime;
		this.content = content;
		this.browseNum = browseNum;
		this.mobileType = mobileType;
		this.photos = photos;
		this.praises = praises;
		this.comments = comments;
	}

	public Speak()
	{
	}
}
