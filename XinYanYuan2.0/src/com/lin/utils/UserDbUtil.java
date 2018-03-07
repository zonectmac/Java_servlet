package com.lin.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lin.model.OBJ;
import com.lin.model.User;
import com.lin.model.UserInfo;

/**
 * �û���Ϣ��������
 * 
 * @author huajian.zhang
 * 
 */
public class UserDbUtil
{
	private static UserDbUtil instance;

	private UserDbUtil()
	{

	}

	public static UserDbUtil getInstance()
	{
		if (null == instance)
		{
			instance = new UserDbUtil();
		}
		return instance;
	}

	/**
	 * �򿪿ɱ����֣����������û���λ����Ϣ��
	 * 
	 * @param userId
	 * @param latitude
	 *            γ��
	 * @param longitude
	 *            ����
	 * @return
	 */
	public OBJ openAllowFind(String userId, double latitude, double longitude)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("_latitude", latitude);
		map.put("_longitude", longitude);
		boolean updateUserInfo = updateUserInfo(map, userId);
		if (updateUserInfo)
			return new OBJ(1, "λ�ø��³ɹ�");
		return new OBJ(-2, "λ�ø���ʧ��");
	}

	/**
	 * �ر��û��ɱ����֣���������û���λ����Ϣ
	 * 
	 * @param userId
	 * @return
	 */
	public OBJ closeAllowFind(String userId)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("_latitude", 0);
		map.put("_longitude", 0);
		boolean updateUserInfo = updateUserInfo(map, userId);
		if (updateUserInfo)
			return new OBJ(1, "�ر��û��ɱ����ֳɹ�");
		return new OBJ(-2, "�ر��û��ɱ�����ʧ��");
	}

	/**
	 * ���ݾ�γ����ѡ
	 * 
	 * @param userId
	 * @param sweepRange
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public List<UserInfo> getNearbyProgrammer(String userId, int sweepRange, double latitude, double longitude)
	{
		List<UserInfo> list = new ArrayList<UserInfo>();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from userinfo");
		sb.append(" where _userId <> " + userId);
		sb.append(" and (2 * 6378.138* ASIN(SQRT(POW(SIN(PI()*(_latitude-");
		sb.append(latitude + ")/360),2)+COS(PI()*");
		sb.append(longitude + "/180)* COS(_latitude * PI()/180)*POW(SIN(PI()*(");
		sb.append(longitude + "-_longitude)/360),2))))<");
		sb.append(sweepRange);
		sb.append(" LIMIT 20");

		try
		{
			Statement statement = DbUtil.openDb().createStatement();
			ResultSet rs = statement.executeQuery(sb.toString());
			while (rs.next())
			{
				String nickName = rs.getString("_nickName");
				String icon = rs.getString("_icon");
				String userPhone = rs.getString("_userPhone");
				String sex = rs.getString("_sex");
				Double longitude2 = rs.getDouble("_longitude");
				Double latitude2 = rs.getDouble("_latitude");
				list.add(new UserInfo(userPhone, nickName, icon, null, null, null, sex, null, null, null, null, null,
						latitude2, longitude2));
			}
			return list;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �����û�����
	 * 
	 * @param userId
	 * @param oldPsw
	 * @param newPsw
	 * @return
	 */
	public OBJ updateUserPassWord(String userId, String oldPsw, String newPsw)
	{
		if (DbUtil.existNumber("users", "_userId=" + userId + " and _password=" + oldPsw) <= 0)
			return new OBJ(-3, "ԭʼ�������");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("_password", newPsw);
		boolean updateTable = DbUtil.updateTable(map, "users", "_userId=" + userId);
		if (updateTable)
			return new OBJ(1, "�޸ĳɹ�");
		return new OBJ(-2, "�޸�ʧ��");
	}

	/**
	 * ��¼
	 * 
	 * @param userPhone
	 *            �û���
	 * @param password
	 *            ����
	 * @return
	 */
	public User login(String userPhone, String password)
	{
		// Ҫִ�е�SQL���
		String sql = "select u._userId,ui._nickName,ui._icon from users u,userinfo ui where u._userPhone = '"
				+ userPhone + "' and u._password='" + password + "' and u._userId = ui._userId";
		System.out.println(sql);
		try
		{
			Statement statement = DbUtil.openDb().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
			{
				int userId = rs.getInt("u._userId");
				String nickName = rs.getString("ui._nickName");
				String icon = rs.getString("ui._icon");
				return new User(userId, nickName, DbUtil.HTTP_ADDRESS + icon);
			}
			rs.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ע���û�
	 * 
	 * @param nick
	 * @param psw
	 * @param sex
	 * @param sign
	 * @param photo
	 * @return
	 */
	public boolean regist(String userPhone, String password, String channel, String termno)
	{
		try
		{
			Statement statement = DbUtil.openDb().createStatement();
			String sql = "insert into users (_userPhone,_password,_channel,_termno) values ('" + userPhone + "','"
					+ password + "','" + channel + "','" + termno + "');";
			int row = statement.executeUpdate(sql);
			if (row > 0)
			{
				statement.close();
				return true;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �����û�����/�����û���ϸ��Ϣ
	 * 
	 * @param map
	 * @param id
	 * @return
	 */
	public boolean updateUserInfo(Map<String, Object> map, String userId)
	{
		if (!map.containsKey("_userId"))
		{
			map.put("_userId", userId);
		}
		if (DbUtil.existNumber("userinfo", "_userId=" + userId) <= 0)
		{ // ��������ڸ�����Ϣ���Ͳ���
			return DbUtil.inserIntoTable(map, "userinfo");
		} else
		{
			map.remove("_userId");
			return DbUtil.updateTable(map, "userinfo", "_userId=" + userId);
		}
	}

	/**
	 * ��ȡ�û���Ϣ
	 * 
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfo(String userId, String userPhone)
	{
		String sql = "select * from userinfo where _userId = " + userId;
		if (TextUtils.isStringsNulls(userId))
		{
			sql = "select * from userinfo where _userPhone = " + userPhone;
		}
		try
		{
			Statement statement = DbUtil.openDb().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
			{
				String nickName = rs.getString("_nickName");
				String icon = rs.getString("_icon");
				userPhone = rs.getString("_userPhone");
				String background = rs.getString("_background");
				String realName = rs.getString("_realName");
				String personExplain = rs.getString("_personExplain");
				String sex = rs.getString("_sex");
				String birthDay = rs.getString("_birthDay");
				String hometown = rs.getString("_hometown");
				String location = rs.getString("_location");
				String mobilePhone = rs.getString("_mobilePhone");
				String email = rs.getString("_email");
				Double longitude = rs.getDouble("_longitude");
				Double latitude = rs.getDouble("_latitude");
				return new UserInfo(userPhone, nickName, DbUtil.HTTP_ADDRESS + icon, DbUtil.HTTP_ADDRESS + background,
						realName, personExplain, sex, birthDay, hometown, location, mobilePhone, email, latitude,
						longitude);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ�û���Ϣ
	 * 
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfo2userId(String userId)
	{
		return getUserInfo(userId, null);
	}

	/**
	 * ��ȡ�û���Ϣ
	 * 
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfo2UserPhone(String userPhone)
	{
		return getUserInfo(null, userPhone);
	}

	/**
	 * ͨ���û�ID ��ȡ�ǳ�
	 * 
	 * @param userId
	 *            s �û�ID
	 * @return
	 */
	public String getNickName(String userId)
	{
		UserInfo userInfo = getUserInfo2userId(userId);
		return userInfo == null ? null : userInfo.getNickName();
	}

	/**
	 * ����û��Ƿ��Ѿ�����
	 * 
	 * @param userPhone
	 *            �ֻ�����
	 * @return
	 */
	public boolean isUserExist(String userPhone)
	{
		return getId(userPhone) != -1;
	}

	/**
	 * ����û��Ƿ��Ѿ�����
	 * 
	 * @param userId
	 *            �û�ID
	 * @return
	 */
	public boolean isUserIdExist(String userId)
	{
		return DbUtil.existNumber("users", "_userId = " + userId) > 0;
	}

	/**
	 * ͨ���ֻ�������ID
	 * 
	 * @param userPhote
	 * @return
	 */
	public int getId(String userPhone)
	{
		String sql = "select * from users where _userPhone = " + userPhone;
		int count = -1;
		try
		{
			Statement statement = DbUtil.openDb().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
			{
				count = rs.getInt("_userId");
			}
			rs.close();
			statement.close();
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return count;
	}
}
