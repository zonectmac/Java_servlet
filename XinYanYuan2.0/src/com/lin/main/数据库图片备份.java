package com.lin.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class 数据库图片备份
{
	File photoBeiFen = new File("D:\\apachetomcat666\\webapps\\XinYanYuan服务器图片备份\\photo");
	File speakPhotoBeiFen = new File("D:\\apachetomcat666\\webapps\\XinYanYuan服务器图片备份\\speakPhoto");
	File photo = new File("D:\\apachetomcat666\\webapps\\XinYanYuan\\photo");
	File speakPhoto = new File("D:\\apachetomcat666\\webapps\\XinYanYuan\\speakPhoto");

	private void isFileNoExistsCreateDirectory(File... f)
	{
		for (File file : f)
		{
			if (!file.exists())
			{
				file.mkdirs();
			}
		}
	}

	public 数据库图片备份()
	{
		isFileNoExistsCreateDirectory(photoBeiFen, speakPhotoBeiFen, photo, speakPhoto);
		for (File f : photoBeiFen.listFiles())
			if (f.isFile())
				listFileName.add(f.getName());
		for (File f : speakPhotoBeiFen.listFiles())
			if (f.isFile())
				listFileName.add(f.getName());
		new Thread()
		{
			public void run()
			{
				while (true)
				{
					try
					{
						imageCopy();
						Thread.sleep(180000);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private List<String> listFileName = new ArrayList<String>();

	private boolean isFind(String fileName)
	{
		for (String n : listFileName)
			if (listFileName.contains(n))
				return true;
		return false;
	}

	private void imageCopy()
	{
		for (File image : photo.listFiles())
		{
			if (image.isFile())
			{
				String name = image.getName();
				if (isFind(name))
					continue;
				File out = new File(photoBeiFen, name);
				copyFile(image, out);
			}
		}
		for (File image : speakPhoto.listFiles())
		{
			if (image.isFile())
			{
				String name = image.getName();
				if (isFind(name))
					continue;
				File out = new File(speakPhotoBeiFen, name);
				copyFile(image, out);
			}
		}
	};

	private void copyFile(File in, File out)
	{
		try
		{
			FileInputStream fis = new FileInputStream(in);
			FileOutputStream fos = new FileOutputStream(out);
			byte b[] = new byte[1024];
			int len = 0;
			while ((len = fis.read(b)) > 0)
			{
				fos.write(b, 0, len);
			}
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
