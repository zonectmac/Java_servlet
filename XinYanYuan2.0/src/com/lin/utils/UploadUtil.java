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
 * �����ϴ��ļ�������
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
	 *            ����·�����磺D:/blog/photo/
	 * @param saveName
	 *            �����ļ������磺acb.jpg(��׺����ʡ��)
	 * @return
	 */
	public static Map<String, Object> saveIconOrBg(HttpServletRequest req, String savePath, String saveName)
	{
		final long MAX_SIZE = 3 * 1024 * 1024;// �����ϴ��ļ����Ϊ 3M
		// ʵ����һ��Ӳ���ļ�����,���������ϴ����ServletFileUpload
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setSizeThreshold(4096);// �����ϴ��ļ�ʱ������ʱ����ļ����ڴ��С,������4K.���ڵĲ��ֽ���ʱ����Ӳ��
		// �ж���ʱ�ļ����Ƿ���ڣ��������򴴽���ʱ�ļ���
		isDirsExist(savePath + "/temp");
		dfif.setRepository(new File(savePath + "/temp"));
		// ���ô����ʱ�ļ���Ŀ¼,web��Ŀ¼�µ�ImagesUploadTempĿ¼
		// �����Ϲ���ʵ�����ϴ����
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		sfu.setSizeMax(MAX_SIZE);// ��������ϴ��ߴ�
		try
		{
			// ��request�õ� ���� �ϴ�����б�
			List<FileItem> fileList = sfu.parseRequest(req);
			// �õ������ϴ����ļ�66
			Iterator<FileItem> fileItr = fileList.iterator();
			String u_name = "";
			Map<String, Object> map = new HashMap<String, Object>();
			String fileName = "";
			// ѭ�����������ļ�
			while (fileItr.hasNext())
			{
				// �õ���ǰ�ļ�
				FileItem fileItem = fileItr.next();
				// ���Լ�form�ֶζ������ϴ�����ļ���(<input type="text" />��)
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
				// �����ļ�
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
	 *            ����·�����磺D:/blog/photo/
	 * @param saveName
	 *            �����ļ������磺acb.jpg(��׺����ʡ��)
	 * @return
	 */
	public static Map<String, Object> saveSpeakImages(HttpServletRequest req, String savePath)
	{
		final long MAX_SIZE = 3 * 1024 * 1024;// �����ϴ��ļ����Ϊ 3M
		// ʵ����һ��Ӳ���ļ�����,���������ϴ����ServletFileUpload
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setSizeThreshold(4096);// �����ϴ��ļ�ʱ������ʱ����ļ����ڴ��С,������4K.���ڵĲ��ֽ���ʱ����Ӳ��
		// �ж���ʱ�ļ����Ƿ���ڣ��������򴴽���ʱ�ļ���
		isDirsExist(savePath + "/temp");
		dfif.setRepository(new File(savePath + "/temp"));
		// ���ô����ʱ�ļ���Ŀ¼,web��Ŀ¼�µ�ImagesUploadTempĿ¼
		// �����Ϲ���ʵ�����ϴ����
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		sfu.setSizeMax(MAX_SIZE);// ��������ϴ��ߴ�
		try
		{
			// ��request�õ� ���� �ϴ�����б�
			List<FileItem> fileList = sfu.parseRequest(req);
			// �õ������ϴ����ļ�66
			Iterator<FileItem> fileItr = fileList.iterator();
			String u_name = "";
			Map<String, Object> map = new HashMap<String, Object>();
			List<String> list = new ArrayList<String>();
			// ѭ�����������ļ�
			while (fileItr.hasNext())
			{
				// �õ���ǰ�ļ�
				FileItem fileItem = fileItr.next();
				// ���Լ�form�ֶζ������ϴ�����ļ���(<input type="text" />��)
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
				// �����ļ�
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

	// ��Ⲣ�����ļ���
	public static boolean isDirsExist(String path)
	{
		File file = new File(path);
		if (file.exists())
			return true;
		return file.mkdirs();
	}
}
