package com.lin.main;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 
 * RSAEncrypt
 * 
 * @author chen
 * 
 * @see
 */

public class Rsaencrype
{
	/**
	 * 
	 * Main method for RSAEncrypt.
	 * 
	 * @param args
	 */

	public static void main(String[] args)
	{
		String[] fileName = new String[] { "D:/hapfish/ShellFolder.java", "D:/hapfish/ShellFolder - ����.java",
				"E:/ShellFolder - ����.java", "E:/ShellFolder.txt", "D:/hapfish/ShellFolder.jpg",
				"E:/ShellFolder�����ַ�.txt", "D:/hapfish/birosoft.jar" };
		FileHashUtil files = new FileHashUtil();
		for (int i = 0; i < fileName.length; i++)
		{
			try
			{
				files.MD5File(fileName[i]);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static final char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };
	public static final String[] hashTypes = new String[] { "MD2", "MD5", "SHA1", "SHA-256", "SHA-384", "SHA-512" };

	static class FileHashUtil
	{

		public void MD5File(String fileName) throws Exception
		{
			// String fileName = args[0];
			System.out.println("��Ҫ��ȡhash���ļ�Ϊ����" + fileName);
			java.util.List<MessageDigest> mds = new java.util.ArrayList<MessageDigest>();
			for (String hashType : hashTypes)
			{
				MessageDigest md = MessageDigest.getInstance(hashType);
				mds.add(md);
			}
			InputStream fis = null;
			try
			{
				fis = new FileInputStream(fileName);
				byte[] buffer = new byte[1024];
				int numRead = 0;
				while ((numRead = fis.read(buffer)) > 0)
				{
					for (MessageDigest md : mds)
					{
						md.update(buffer, 0, numRead);
					}
				}
			} catch (Exception ex)
			{
				ex.printStackTrace();
			} finally
			{
				if (fis != null)
				{
					fis.close();
				}
			}
			for (MessageDigest md : mds)
			{
				System.out.println(md.getAlgorithm() + " == " + toHexString(md.digest()));
			}
		}

		public String toHexString(byte[] b)
		{
			StringBuilder sb = new StringBuilder(b.length * 2);
			for (int i = 0; i < b.length; i++)
			{
				sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
				sb.append(hexChar[b[i] & 0x0f]);
			}
			return sb.toString();
		}

	}

}
