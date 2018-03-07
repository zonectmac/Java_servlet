package com.lin.model;

import java.util.ArrayList;
import java.util.List;

public class UsersManager
{
	private List<String> listUserIds = new ArrayList<String>();

	public synchronized void userLogin(int userId)
	{
		listUserIds.add(userId + "");
	}

	public synchronized boolean userOutLogin(int userId)
	{
		return listUserIds.remove(userId + "");
	}
}
