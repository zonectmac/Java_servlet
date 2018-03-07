package com.lin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lin.utils.TextUtils;

public class xmlServlet extends BaseServlet
{

	@Override
	protected void doGetAndPost(HttpServletRequest req, HttpServletResponse resp)
	{
		// TODO Auto-generated method stub
		String typeStr = req.getServletPath().substring(1);
		System.out.println(typeStr+"=====");
		if (TextUtils.equals(typeStr, "xmlServlet"))
		{
			repairs(req);
		}
	}

	private void repairs(HttpServletRequest req)
	{
		String strxml = req.getParameter("strxml");
		if (TextUtils.isStringsNulls(strxml))
		{
			writeOBJ(-5, "参数错误");
			return;
		}else
		{
			writeOBJ(1, "成功");
			System.out.println("b =="+strxml);
			//write(JSON.toJSONString(new OBJList<User>(1, "登录成功", login)));
		}
		
	}

	/**
	 * Constructor of the object.
	 */
	public xmlServlet()
	{
		super();
	}

	
}
