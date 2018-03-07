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
 * 数据库打开工具
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
			// URL指向要访问的数据库名diary_db
			String url = "jdbc:mysql://" + ip + ":3306/test?Unicode=true&characterEncoding=gbk";
			// MySQL配置时的用户名
			String user = "root";
			// Java连接MySQL配置时的密码
			String password = "root";
			try
			{
				// 加载驱动程序
				Class.forName(driver);
				// 连续数据库
				instance = DriverManager.getConnection(url, user, password);
			} catch (ClassNotFoundException e)
			{
				 //数据库驱动类异常处理
	            System.out.println("Sorry,can`t find the Driver!");   
	            e.printStackTrace();   
	            } catch(SQLException e) {
	            //数据库连接失败异常处理
	            e.printStackTrace();  
	            }catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }finally{
	            System.out.println("数据库数据成功获取！！");
	        }
		}
		return instance;
	}

	/**
	 * 查询某一张表格 某种条件存在的个数
	 * 
	 * @param tableName
	 *            表名
	 * @param whele
	 *            条件 可以为null
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
	 * 插入数据
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
	 * 跟新数据表
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
			// 取消最后一个逗号
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
	 * 从表中删除数据
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
