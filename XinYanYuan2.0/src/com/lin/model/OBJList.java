package com.lin.model;

import java.util.ArrayList;
import java.util.List;

public class OBJList<T> extends OBJ
{
	private List<T> list = null;

	public OBJList(int code, String message, List<T> list)
	{
		super(code, message);
		this.list = list;
	}

	public List<T> getList()
	{
		return list;
	}

	public void setList(List<T> list)
	{
		this.list = list;
	}

	@Override
	public String toString()
	{
		return "OBJList [list=" + list + ", code=" + code + ", message=" + message + "]";
	}

	public OBJList(int code, String message, T t)
	{
		super(code, message);
		if (list == null)
		{
			list = new ArrayList<T>();
		}
		list.add(t);
	}
}
