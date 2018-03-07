package com.lin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.lin.model.OBJList;
import com.lin.model.User;
import com.lin.utils.TextUtils;
import com.lin.utils.UserDbUtil;

public class repair extends BaseServlet
{

	@Override
	protected void doGetAndPost(HttpServletRequest req, HttpServletResponse resp)
	{
		// TODO Auto-generated method stub
		String typeStr = req.getServletPath().substring(1);
		System.out.println(typeStr+"=====");
		if (TextUtils.equals(typeStr, "repair"))
		{
			repairs(req);
		}
	}

	private void repairs(HttpServletRequest req)
	{

		String broad = req.getParameter("broad");
		String reason = req.getParameter("reason");
		String repari_method = req.getParameter("repari_method");
		if (TextUtils.isStringsNulls(broad, reason,repari_method))
		{
			writeOBJ(-5, "参数错误");
			return;
		}else
		{
			writeOBJ(1, "成功");
			System.out.println("b =="+broad+"== r== "+reason+"== re =="+repari_method);
			//write(JSON.toJSONString(new OBJList<User>(1, "登录成功", login)));
		}
	}

	
}
