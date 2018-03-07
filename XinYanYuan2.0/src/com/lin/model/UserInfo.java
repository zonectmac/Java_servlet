package com.lin.model;

import com.lin.utils.TextUtils;

public class UserInfo
{
	private String userPhone;
	private String nickName;
	private String icon;
	private String background;
	private String realName;
	private String personExplain;
	private String sex;
	private int age;
	private String birthDay;
	private String constellation;
	private String hometown;
	private String location;
	private String mobilePhone;
	private String email;
	private Double latitude;
	private Double longitude;

	public Double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}

	public Double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(Double longitude)
	{
		this.longitude = longitude;
	}

	@Override
	public String toString()
	{
		return "UserInfo [userPhone=" + userPhone + ", nickName=" + nickName + ", icon=" + icon + ", background="
				+ background + ", realName=" + realName + ", personExplain=" + personExplain + ", sex=" + sex
				+ ", age=" + age + ", birthDay=" + birthDay + ", constellation=" + constellation + ", hometown="
				+ hometown + ", location=" + location + ", mobilePhone=" + mobilePhone + ", email=" + email + "]";
	}

	public UserInfo(String userPhone, String nickName, String icon, String background, String realName,
			String personExplain, String sex, String birthDay, String hometown, String location, String mobilePhone,
			String email, Double latitude, Double longitude)
	{
		setLatitude(latitude);
		setLongitude(longitude);
		this.userPhone = userPhone;
		this.nickName = nickName;
		this.icon = icon;
		this.background = background;
		this.realName = realName;
		this.personExplain = personExplain;
		this.sex = sex;
		if (!TextUtils.isStringsNulls(birthDay))
		{
			setAge(Long.parseLong(birthDay));
			setConstellation(Long.parseLong(birthDay));
		}
		this.birthDay = birthDay;
		this.hometown = hometown;
		this.location = location;
		this.mobilePhone = mobilePhone;
		this.email = email;

	}

	public String getUserPhone()
	{
		return userPhone;
	}

	public void setUserPhone(String userPhone)
	{
		this.userPhone = userPhone;
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

	public String getBackground()
	{
		return background;
	}

	public void setBackground(String background)
	{
		this.background = background;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public String getPersonExplain()
	{
		return personExplain;
	}

	public void setPersonExplain(String personExplain)
	{
		this.personExplain = personExplain;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(long age)
	{
		this.age = TextUtils.getAge(age);
	}

	public String getBirthDay()
	{
		return birthDay;
	}

	public void setBirthDay(String birthDay)
	{
		this.birthDay = birthDay;
	}

	public String getConstellation()
	{
		return constellation;
	}

	public void setConstellation(long constellation)
	{
		this.constellation = TextUtils.getStarSeat(constellation);
	}

	public String getHometown()
	{
		return hometown;
	}

	public void setHometown(String hometown)
	{
		this.hometown = hometown;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getMobilePhone()
	{
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public UserInfo()
	{
	}
}
