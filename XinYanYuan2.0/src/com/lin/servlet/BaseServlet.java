package com.lin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.lin.model.OBJ;

public abstract class BaseServlet extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletResponse resp = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		resp.setContentType("text/html;charset=UTF-8");
		this.resp = resp;
		doGetAndPost(req, resp);
	}

	protected void writeOBJ(int code, String message)
	{
		write(JSON.toJSONString(new OBJ(code, message)));
	}

	protected void write(String json)
	{
		System.out.println(json);
		try
		{
			PrintWriter pw = resp.getWriter();
			pw.write(json);
			pw.flush();
			pw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected abstract void doGetAndPost(HttpServletRequest req, HttpServletResponse resp);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		doGet(req, resp);
	}
}
