package com.lin.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ���ݿ�򿪹���
 * 
 * @author
 * 
 */
public class DbUtil
{
	// private final static String ip = "192.168.1.184";
	private final static String ip = "192.168.191.1";
	private final static String port = "8080";
	public static final String HTTP_ADDRESS = "http://" + ip + ":" + port + "/XinYanYuan";
	private static Connection instance;
	public static Connection openDb()
	{
		if (null == instance)
		{
			String driver = "com.mysql.jdbc.Driver";
			// URLָ��Ҫ���ʵ����ݿ���diary_db
			String url = "jdbc:mysql://" + ip + ":3306/test?Unicode=true&characterEncoding=gbk";
			// MySQL����ʱ���û���
			String user = "root";
			// Java����MySQL����ʱ������
			String password = "root";
			try
			{
				// ������������
				Class.forName(driver);
				// �������ݿ�
				instance = DriverManager.getConnection(url, user, password);
			} catch (ClassNotFoundException e)
			{
				 //���ݿ��������쳣����
	            System.out.println("Sorry,can`t find the Driver!");   
	            e.printStackTrace();   
	            } catch(SQLException e) {
	            //���ݿ�����ʧ���쳣����
	            e.printStackTrace();  
	            }catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }finally{
	            System.out.println("���ݿ����ݳɹ���ȡ����");
	        }
		}
		return instance;
	}

	/**
	 * ��ѯĳһ�ű�� ĳ���������ڵĸ���
	 * 
	 * @param tableName
	 *            ����
	 * @param whele
	 *            ���� ����Ϊnull
	 * @return
	 */
	public static int existNumber(String tableName, String where)
	{
		String sql = "select count(*) from " + tableName;
		if (!TextUtils.isStringsNulls(where))
		{
			sql += " where " + where;
		}
		int count = 0;
		try
		{
			Statement statement = openDb().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
			{
				count = rs.getInt("count(*)");
			}
			rs.close();
			statement.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * ��������
	 * 
	 * @param map
	 * @param tableName
	 * @return
	 */
	public static boolean inserIntoTable(Map<String, Object> map, String tableName)
	{
		if (map.size() > 0)
		{
			StringBuffer sb = new StringBuffer("insert into ");
			sb.append(tableName).append(" ");
			Iterator<Entry<String, Object>> it = map.entrySet().iterator();
			StringBuffer columns = new StringBuffer("(");
			StringBuffer values = new StringBuffer("(");
			while (it.hasNext())
			{
				Entry<String, Object> entry = it.next();
				columns.append(entry.getKey()).append(",");
				Object obj = entry.getValue();
				if (obj instanceof String)
				{
					values.append("'").append(obj.toString()).append("',");
				} else
				{
					values.append(obj).append(",");
				}
			}
			columns.delete(columns.length() - 1, columns.length());
			columns.append(")");
			values.delete(values.length() - 1, values.length());
			values.append(")");
			sb.append(columns).append(" values ").append(values);
			String sql = sb.toString();
			System.out.println(sql);
			try
			{
				Statement statement = openDb().createStatement();
				int row = statement.executeUpdate(sql);
				if (row > 0)
				{
					System.out.println("row:" + row);
					return true;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * �������ݱ�
	 * 
	 * @param map
	 * @param where
	 * @return
	 */
	public static boolean updateTable(Map<String, Object> map, String tableName, String where)
	{
		if (map.size() > 0)
		{
			StringBuffer sb = new StringBuffer("update " + tableName + " set ");
			Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<String, Object> entry = it.next();
				sb.append(entry.getKey()).append("=");
				Object obj = entry.getValue();
				if (obj instanceof String)
				{
					sb.append("'").append(TextUtils.ISO2UTF8(obj.toString())).append("',");
				} else
				{
					sb.append(obj).append(",");
				}
			}
			// ȡ�����һ������
			sb.deleteCharAt(sb.length() - 1);
			if (null != where)
				sb.append(" where ").append(where);
			String sql = sb.toString();
			System.out.println(sql);
			try
			{
				Statement statement = DbUtil.openDb().createStatement();
				int row = statement.executeUpdate(sql);
				if (row > 0)
				{
					return true;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * �ӱ���ɾ������
	 * 
	 * @param tableName
	 * @param where
	 * @return
	 */
	public static boolean deleteTable(String tableName, String where)
	{
		StringBuffer sb = new StringBuffer("delete from ");
		sb.append(tableName).append(" ");
		if (null != where)
		{
			sb.append("where ").append(where);
		}
		try
		{
			Statement statement = DbUtil.openDb().createStatement();
			int row = statement.executeUpdate(sb.toString());
			if (row > 0)
			{
				return true;
			}
		} catch (Exception e)
		{

		}
		return false;
	}
}
