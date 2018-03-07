package com.lin.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.lin.model.OBJ;
import com.lin.model.OBJList;
import com.lin.model.User;
import com.lin.model.UserInfo;
import com.lin.utils.DbUtil;
import com.lin.utils.TextUtils;
import com.lin.utils.UploadUtil;
import com.lin.utils.UserDbUtil;

public class UserServlet extends BaseServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGetAndPost(HttpServletRequest req, HttpServletResponse resp)
	{
		String typeStr = req.getServletPath().substring(1);// ��õ��ֶ���ǰ����һ��'/'����Ҫ��1��λ�ÿ�ʼ��ȡ
		if (TextUtils.equals(typeStr, "userRegister"))
		{// ע��
			register(req);
		} else if (TextUtils.equals(typeStr, "userLogin"))
		{// ��¼
			userLogin(req);
		} else if (TextUtils.equals(typeStr, "getUserInfo"))
		{// ��ȡ�û���Ϣ
			getUserInfo(req);
		} else if (TextUtils.equals(typeStr, "updateUserInfo"))
		{ // �����û���Ϣ
			updateUserInfo(req);
		} else if (TextUtils.equals(typeStr, "updateIcon"))
		{ // �����û�ͷ��
			updateImage(req, true);
		} else if (TextUtils.equals(typeStr, "updateBackground"))
		{// �����û�����ͼƬ
			updateImage(req, false);
		} else if (TextUtils.equals(typeStr, "updateUserPassWord"))
		{// �޸ĵ�¼����
			updateUserPassWord(req);
		} else if (TextUtils.equals(typeStr, "openAllowFind"))
		{
			// �򿪿ɱ����֣����ܶ�λ��Ϣ
			openAllowFind(req);
		} else if (TextUtils.equals(typeStr, "closeAllowFind"))
		{
			// �رտɱ����֣����ܶ�λ��Ϣ
			closeAllowFind(req);
		} else if (TextUtils.equals(typeStr, "getNearbyProgrammer"))
		{
			// ��ȡ��������
			getNearbyProgrammer(req);
		}
	}

	private void getNearbyProgrammer(HttpServletRequest req)
	{
		// ��ȡ�û�ID
		String userId = req.getParameter("userId");
		String sweepRangeStr = req.getParameter("sweepRange");
		String latitudeStr = req.getParameter("latitude");
		String longitudeStr = req.getParameter("longitude");
		if (TextUtils.isStringsNulls(userId, latitudeStr, longitudeStr, sweepRangeStr))
		{
			writeOBJ(-5, "��������");
			return;
		}
		try
		{
			int sweepRange = Integer.parseInt(sweepRangeStr);
			double latitude = Double.parseDouble(latitudeStr);
			double longitude = Double.parseDouble(longitudeStr);
			List<UserInfo> list = UserDbUtil.getInstance().getNearbyProgrammer(userId, sweepRange, latitude, longitude);
			if (list == null)
				writeOBJ(-2, "��ȡʧ��");
			if (list.size() == 0)
				writeOBJ(-3, "���޸�������");
			write(JSON.toJSONString(new OBJList<UserInfo>(1, "��ȡ�ɹ�", list)));
		} catch (Exception e)
		{
			writeOBJ(-5, "��������");
			return;
		}
	}

	private void openAllowFind(HttpServletRequest req)
	{
		// ��ȡ�û�ID
		String userId = req.getParameter("userId");
		String latitudeStr = req.getParameter("latitude");
		String longitudeStr = req.getParameter("longitude");
		if (TextUtils.isStringsNulls(userId, latitudeStr, longitudeStr))
		{
			writeOBJ(-5, "��������");
			return;
		}
		try
		{
			double latitude = Double.parseDouble(latitudeStr);
			double longitude = Double.parseDouble(longitudeStr);
			OBJ openAllowFind = UserDbUtil.getInstance().openAllowFind(userId, latitude, longitude);
			write(JSON.toJSONString(openAllowFind));
		} catch (Exception e)
		{
			writeOBJ(-6, "��γ�Ȳ�������");
			return;
		}
	}

	private void closeAllowFind(HttpServletRequest req)
	{
		// ��ȡ�û�ID
		String userId = req.getParameter("userId");
		if (TextUtils.isStringsNulls(userId))
		{
			writeOBJ(-5, "��������");
			return;
		}
		OBJ openAllowFind = UserDbUtil.getInstance().closeAllowFind(userId);
		write(JSON.toJSONString(openAllowFind));
	}

	/**
	 * �޸�����
	 * 
	 * @param req
	 */
	private void updateUserPassWord(HttpServletRequest req)
	{
		// ��ȡ�û�ID
		String userId = req.getParameter("userId");
		String oldPsw = req.getParameter("oldPsw");
		String newPsw = req.getParameter("newPsw");
		if (TextUtils.isStringsNulls(userId, oldPsw, newPsw))
		{
			writeOBJ(-5, "��������");
			return;
		}
		write(JSON.toJSONString(UserDbUtil.getInstance().updateUserPassWord(userId, oldPsw, newPsw)));
	}

	private void updateImage(HttpServletRequest req, boolean isIcon)
	{
		// ��ȡ�û�ID
		String name = "background";
		if (isIcon)
		{
			name = "icon";
		}
		String contentType = req.getContentType();
		if (contentType.contains("multipart/form-data") || contentType.contains("multipart/mixed"))
		{
			String path = getServletContext().getRealPath("/photo");
			Map<String, Object> map = UploadUtil.saveIconOrBg(req, path, name);

			// �������ɹ�ͼƬ,���ر����·�����������ݿ�д��ͼƬ�ĵ�ַ
			boolean updateUserInfo = UserDbUtil.getInstance().updateUserInfo(map, map.get("_userId").toString());
			if (updateUserInfo)
			{
				write(JSON.toJSONString(new OBJList<String>(1, "�޸ĳɹ�", DbUtil.HTTP_ADDRESS
						+ map.get("_" + name).toString())));
			} else
				writeOBJ(-4, "�޸�ʧ��,���ݿ�д��ʧ��");
		} else
			writeOBJ(-2, "�޸�ʧ��,��������");
	}

	private void userLogin(HttpServletRequest req)
	{
		// ��ȡ�û���
		String userPhone = req.getParameter("userPhone");
		// ��ȡ����
		String password = req.getParameter("password");
		if (TextUtils.isStringsNulls(userPhone, password))
		{
			writeOBJ(-5, "��������");
			return;
		}
		User login = UserDbUtil.getInstance().login(userPhone, password);
		if (login == null)
		{
			writeOBJ(-2, "��¼ʧ��");

		} else
		{
			write(JSON.toJSONString(new OBJList<User>(1, "��¼�ɹ�", login)));
		}
	}

	private void getUserInfo(HttpServletRequest req)
	{
		String userPhone = req.getParameter("userPhone");
		if (TextUtils.isStringsNulls(userPhone))
		{
			writeOBJ(-5, "��������");
			return;
		}
		if (true)
		{
			/******************* ����Ӧ���ж��û��Ƿ��¼ ***************************/
		}
		if (!UserDbUtil.getInstance().isUserExist(userPhone))
		{
			writeOBJ(-4, "�û�������");
			return;
		}
		UserInfo userInfo2UserPhone = UserDbUtil.getInstance().getUserInfo2UserPhone(userPhone);
		if (userInfo2UserPhone != null)
		{
			write(JSON.toJSONString(new OBJList<UserInfo>(1, "��ȡ�ɹ�", userInfo2UserPhone)));
		} else
		{
			writeOBJ(1, "��ȡʧ��");
		}
	}

	private void updateUserInfo(HttpServletRequest req)
	{
		String userId = req.getParameter("userId");
		if (TextUtils.isStringsNulls(userId))
		{
			writeOBJ(-5, "��������");
			return;
		}
		if (true)
		{
			/******************* ����Ӧ���ж��û��Ƿ��¼ ***************************/
		}
		if (!UserDbUtil.getInstance().isUserIdExist(userId))
		{
			writeOBJ(-4, "�û�������");
			return;
		}
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		String nickName = req.getParameter("nickName");
		if (!TextUtils.isStringsNulls(nickName))
			hashMap.put("_nickName", nickName);
		String realName = req.getParameter("realName");
		if (!TextUtils.isStringsNulls(realName))
			hashMap.put("_realName", realName);
		String personExplain = req.getParameter("personExplain");
		if (!TextUtils.isStringsNulls(personExplain))
			hashMap.put("_personExplain", personExplain);
		String sex = req.getParameter("sex");
		if (!TextUtils.isStringsNulls(sex))
			hashMap.put("_sex", sex);
		String birthDay = req.getParameter("birthDay");
		if (!TextUtils.isStringsNulls(birthDay))
			hashMap.put("_birthDay", birthDay);
		String hometown = req.getParameter("hometown");
		if (!TextUtils.isStringsNulls(hometown))
			hashMap.put("_hometown", hometown);
		String location = req.getParameter("location");
		if (!TextUtils.isStringsNulls(location))
			hashMap.put("_location", location);
		String email = req.getParameter("email");
		if (!TextUtils.isStringsNulls(email))
			hashMap.put("_email", email);
		String mobilePhone = req.getParameter("mobilePhone");
		if (!TextUtils.isStringsNulls(mobilePhone))
			hashMap.put("_mobilePhone", mobilePhone);
		boolean updateUserInfo = UserDbUtil.getInstance().updateUserInfo(hashMap, userId);
		if (updateUserInfo)
		{
			write(JSON.toJSONString(new OBJList<UserInfo>(1, "�޸ĳɹ�", UserDbUtil.getInstance()
					.getUserInfo2userId(userId))));
		} else
		{
			writeOBJ(-2, "�޸�ʧ��");
		}
	}

	private void register(HttpServletRequest req)
	{
		String userPhone = req.getParameter("userPhone");
		String password = req.getParameter("password");
		String channel = req.getParameter("channel");
		String termno = req.getParameter("termno");
		if (TextUtils.isStringsNulls(userPhone, password, channel, termno))
		{
			writeOBJ(-5, "��������");
			return;
		}
		boolean userExist = UserDbUtil.getInstance().isUserExist(userPhone);
		if (userExist)
		{
			writeOBJ(-3, "�û����Ѿ����ڲ���ʹ��");
		} else
		{
			boolean regist = UserDbUtil.getInstance().regist(userPhone, password, channel, termno);
			if (regist)
			{
				writeOBJ(1, "ע��ɹ�");
				/** ����Ӧ�ô���һ���û���Ӧ����ϸ��Ϣ **/
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("_userPhone", userPhone);
				hashMap.put("_mobilePhone", userPhone);
				UserDbUtil.getInstance().updateUserInfo(hashMap, UserDbUtil.getInstance().getId(userPhone) + "");
			} else
			{
				writeOBJ(-2, "ע��ʧ��");
			}
		}
	}

}