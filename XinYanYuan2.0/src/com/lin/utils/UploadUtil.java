package com.lin.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 接收上传文件并保存
 * 
 * @author huajian.zhang
 * 
 */
public class UploadUtil
{

	/**
	 * 
	 * @param req
	 * @param savePath
	 *            保存路径，如：D:/blog/photo/
	 * @param saveName
	 *            保存文件名，如：acb.jpg(后缀可以省略)
	 * @return
	 */
	public static Map<String, Object> saveIconOrBg(HttpServletRequest req, String savePath, String saveName)
	{
		final long MAX_SIZE = 3 * 1024 * 1024;// 设置上传文件最大为 3M
		// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setSizeThreshold(4096);// 设置上传文件时用于临时存放文件的内存大小,这里是4K.多于的部分将临时存在硬盘
		// 判断临时文件夹是否存在，不存在则创建临时文件夹
		isDirsExist(savePath + "/temp");
		dfif.setRepository(new File(savePath + "/temp"));
		// 设置存放临时文件的目录,web根目录下的ImagesUploadTemp目录
		// 用以上工厂实例化上传组件
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		sfu.setSizeMax(MAX_SIZE);// 设置最大上传尺寸
		try
		{
			// 从request得到 所有 上传域的列表
			List<FileItem> fileList = sfu.parseRequest(req);
			// 得到所有上传的文件66
			Iterator<FileItem> fileItr = fileList.iterator();
			String u_name = "";
			Map<String, Object> map = new HashMap<String, Object>();
			String fileName = "";
			// 循环处理所有文件
			while (fileItr.hasNext())
			{
				// 得到当前文件
				FileItem fileItem = fileItr.next();
				// 忽略简单form字段而不是上传域的文件域(<input type="text" />等)
				if (fileItem == null)
					continue;
				if (fileItem.isFormField())
				{
					String fieldName = "_" + fileItem.getFieldName();
					System.out.println(fieldName);
					String value = fileItem.getString();
					System.out.println(value);
					map.put(fieldName, value);
					fileName = saveName + value + ".jpg";
					u_name = savePath + "/" + fileName;
					continue;
				}
				// 保存文件
				fileItem.write(new File(u_name));
				System.out.println(u_name);
				map.put("_" + saveName, "/photo/" + fileName);
				System.out.println("---" + map.get("_" + saveName));
			}
			return map;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param req
	 * @param savePath
	 *            保存路径，如：D:/blog/photo/
	 * @param saveName
	 *            保存文件名，如：acb.jpg(后缀可以省略)
	 * @return
	 */
	public static Map<String, Object> saveSpeakImages(HttpServletRequest req, String savePath)
	{
		final long MAX_SIZE = 3 * 1024 * 1024;// 设置上传文件最大为 3M
		// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setSizeThreshold(4096);// 设置上传文件时用于临时存放文件的内存大小,这里是4K.多于的部分将临时存在硬盘
		// 判断临时文件夹是否存在，不存在则创建临时文件夹
		isDirsExist(savePath + "/temp");
		dfif.setRepository(new File(savePath + "/temp"));
		// 设置存放临时文件的目录,web根目录下的ImagesUploadTemp目录
		// 用以上工厂实例化上传组件
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		sfu.setSizeMax(MAX_SIZE);// 设置最大上传尺寸
		try
		{
			// 从request得到 所有 上传域的列表
			List<FileItem> fileList = sfu.parseRequest(req);
			// 得到所有上传的文件66
			Iterator<FileItem> fileItr = fileList.iterator();
			String u_name = "";
			Map<String, Object> map = new HashMap<String, Object>();
			List<String> list = new ArrayList<String>();
			// 循环处理所有文件
			while (fileItr.hasNext())
			{
				// 得到当前文件
				FileItem fileItem = fileItr.next();
				// 忽略简单form字段而不是上传域的文件域(<input type="text" />等)
				if (fileItem == null)
					continue;
				String fileName = "";
				if (fileItem.isFormField())
				{
					String fieldName = "_" + fileItem.getFieldName();
					System.out.println(fieldName);
					String value = fileItem.getString();
					System.out.println(value);
					map.put(fieldName, value);
					continue;
				}
				fileName = "speakImage" + System.currentTimeMillis() + "sp" + new Random().nextInt(2000) + ".jpg";
				u_name = savePath + "/" + fileName;
				// 保存文件
				fileItem.write(new File(u_name));
				System.out.println(u_name);
				list.add("/speakPhoto/" + fileName);
			}
			map.put("list", list);
			return map;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	// 检测并创建文件夹
	public static boolean isDirsExist(String path)
	{
		File file = new File(path);
		if (file.exists())
			return true;
		return file.mkdirs();
	}
}
