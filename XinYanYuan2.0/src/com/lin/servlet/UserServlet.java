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
		String typeStr = req.getServletPath().substring(1);// 获得的字段名前面会多一个'/'所以要从1的位置开始截取
		if (TextUtils.equals(typeStr, "userRegister"))
		{// 注册
			register(req);
		} else if (TextUtils.equals(typeStr, "userLogin"))
		{// 登录
			userLogin(req);
		} else if (TextUtils.equals(typeStr, "getUserInfo"))
		{// 获取用户信息
			getUserInfo(req);
		} else if (TextUtils.equals(typeStr, "updateUserInfo"))
		{ // 更新用户信息
			updateUserInfo(req);
		} else if (TextUtils.equals(typeStr, "updateIcon"))
		{ // 更新用户头像
			updateImage(req, true);
		} else if (TextUtils.equals(typeStr, "updateBackground"))
		{// 更新用户背景图片
			updateImage(req, false);
		} else if (TextUtils.equals(typeStr, "updateUserPassWord"))
		{// 修改登录密码
			updateUserPassWord(req);
		} else if (TextUtils.equals(typeStr, "openAllowFind"))
		{
			// 打开可被发现，接受定位信息
			openAllowFind(req);
		} else if (TextUtils.equals(typeStr, "closeAllowFind"))
		{
			// 关闭可被发现，接受定位信息
			closeAllowFind(req);
		} else if (TextUtils.equals(typeStr, "getNearbyProgrammer"))
		{
			// 获取附近的人
			getNearbyProgrammer(req);
		}
	}

	private void getNearbyProgrammer(HttpServletRequest req)
	{
		// 获取用户ID
		String userId = req.getParameter("userId");
		String sweepRangeStr = req.getParameter("sweepRange");
		String latitudeStr = req.getParameter("latitude");
		String longitudeStr = req.getParameter("longitude");
		if (TextUtils.isStringsNulls(userId, latitudeStr, longitudeStr, sweepRangeStr))
		{
			writeOBJ(-5, "参数错误");
			return;
		}
		try
		{
			int sweepRange = Integer.parseInt(sweepRangeStr);
			double latitude = Double.parseDouble(latitudeStr);
			double longitude = Double.parseDouble(longitudeStr);
			List<UserInfo> list = UserDbUtil.getInstance().getNearbyProgrammer(userId, sweepRange, latitude, longitude);
			if (list == null)
				writeOBJ(-2, "获取失败");
			if (list.size() == 0)
				writeOBJ(-3, "暂无附近的人");
			write(JSON.toJSONString(new OBJList<UserInfo>(1, "获取成功", list)));
		} catch (Exception e)
		{
			writeOBJ(-5, "参数错误");
			return;
		}
	}

	private void openAllowFind(HttpServletRequest req)
	{
		// 获取用户ID
		String userId = req.getParameter("userId");
		String latitudeStr = req.getParameter("latitude");
		String longitudeStr = req.getParameter("longitude");
		if (TextUtils.isStringsNulls(userId, latitudeStr, longitudeStr))
		{
			writeOBJ(-5, "参数错误");
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
			writeOBJ(-6, "经纬度参数错误");
			return;
		}
	}

	private void closeAllowFind(HttpServletRequest req)
	{
		// 获取用户ID
		String userId = req.getParameter("userId");
		if (TextUtils.isStringsNulls(userId))
		{
			writeOBJ(-5, "参数错误");
			return;
		}
		OBJ openAllowFind = UserDbUtil.getInstance().closeAllowFind(userId);
		write(JSON.toJSONString(openAllowFind));
	}

	/**
	 * 修改密码
	 * 
	 * @param req
	 */
	private void updateUserPassWord(HttpServletRequest req)
	{
		// 获取用户ID
		String userId = req.getParameter("userId");
		String oldPsw = req.getParameter("oldPsw");
		String newPsw = req.getParameter("newPsw");
		if (TextUtils.isStringsNulls(userId, oldPsw, newPsw))
		{
			writeOBJ(-5, "参数错误");
			return;
		}
		write(JSON.toJSONString(UserDbUtil.getInstance().updateUserPassWord(userId, oldPsw, newPsw)));
	}

	private void updateImage(HttpServletRequest req, boolean isIcon)
	{
		// 获取用户ID
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

			// 如果保存成功图片,返回保存的路劲。就往数据库写入图片的地址
			boolean updateUserInfo = UserDbUtil.getInstance().updateUserInfo(map, map.get("_userId").toString());
			if (updateUserInfo)
			{
				write(JSON.toJSONString(new OBJList<String>(1, "修改成功", DbUtil.HTTP_ADDRESS
						+ map.get("_" + name).toString())));
			} else
				writeOBJ(-4, "修改失败,数据库写入失败");
		} else
			writeOBJ(-2, "修改失败,参数错误");
	}

	private void userLogin(HttpServletRequest req)
	{
		// 获取用户名
		String userPhone = req.getParameter("userPhone");
		// 获取密码
		String password = req.getParameter("password");
		if (TextUtils.isStringsNulls(userPhone, password))
		{
			writeOBJ(-5, "参数错误");
			return;
		}
		User login = UserDbUtil.getInstance().login(userPhone, password);
		if (login == null)
		{
			writeOBJ(-2, "登录失败");

		} else
		{
			write(JSON.toJSONString(new OBJList<User>(1, "登录成功", login)));
		}
	}

	private void getUserInfo(HttpServletRequest req)
	{
		String userPhone = req.getParameter("userPhone");
		if (TextUtils.isStringsNulls(userPhone))
		{
			writeOBJ(-5, "参数错误");
			return;
		}
		if (true)
		{
			/******************* 这里应该判断用户是否登录 ***************************/
		}
		if (!UserDbUtil.getInstance().isUserExist(userPhone))
		{
			writeOBJ(-4, "用户不存在");
			return;
		}
		UserInfo userInfo2UserPhone = UserDbUtil.getInstance().getUserInfo2UserPhone(userPhone);
		if (userInfo2UserPhone != null)
		{
			write(JSON.toJSONString(new OBJList<UserInfo>(1, "获取成功", userInfo2UserPhone)));
		} else
		{
			writeOBJ(1, "获取失败");
		}
	}

	private void updateUserInfo(HttpServletRequest req)
	{
		String userId = req.getParameter("userId");
		if (TextUtils.isStringsNulls(userId))
		{
			writeOBJ(-5, "参数错误");
			return;
		}
		if (true)
		{
			/******************* 这里应该判断用户是否登录 ***************************/
		}
		if (!UserDbUtil.getInstance().isUserIdExist(userId))
		{
			writeOBJ(-4, "用户不存在");
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
			write(JSON.toJSONString(new OBJList<UserInfo>(1, "修改成功", UserDbUtil.getInstance()
					.getUserInfo2userId(userId))));
		} else
		{
			writeOBJ(-2, "修改失败");
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
			writeOBJ(-5, "参数错误");
			return;
		}
		boolean userExist = UserDbUtil.getInstance().isUserExist(userPhone);
		if (userExist)
		{
			writeOBJ(-3, "用户名已经存在不可使用");
		} else
		{
			boolean regist = UserDbUtil.getInstance().regist(userPhone, password, channel, termno);
			if (regist)
			{
				writeOBJ(1, "注册成功");
				/** 这里应该创建一个用户对应的详细信息 **/
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("_userPhone", userPhone);
				hashMap.put("_mobilePhone", userPhone);
				UserDbUtil.getInstance().updateUserInfo(hashMap, UserDbUtil.getInstance().getId(userPhone) + "");
			} else
			{
				writeOBJ(-2, "注册失败");
			}
		}
	}

}