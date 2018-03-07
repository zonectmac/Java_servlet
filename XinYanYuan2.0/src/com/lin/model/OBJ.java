package com.lin.model;

public class OBJ
{
	protected int code;
	protected String message;

	public OBJ(int code, String message)
	{
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString()
	{
		return "OBJ [code=" + code + ", message=" + message + "]";
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}
