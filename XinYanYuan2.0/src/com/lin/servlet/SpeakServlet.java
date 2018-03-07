package com.lin.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.lin.model.OBJ;
import com.lin.model.OBJList;
import com.lin.model.Speak;
import com.lin.utils.MessagePush;
import com.lin.utils.SpeakDbUtil;
import com.lin.utils.TextUtils;
import com.lin.utils.UploadUtil;
import com.lin.utils.UserDbUtil;

public class SpeakServlet extends BaseServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGetAndPost(HttpServletRequest req, HttpServletResponse resp)
	{
		String typeStr = req.getServletPath().substring(1);// ��õ��ֶ���ǰ����һ��'/'����Ҫ��1��λ�ÿ�ʼ��ȡ
		if (TextUtils.equals(typeStr, "publishSpeak"))
		{ // ����˵˵
			publishSpeak(req);
		} else if (TextUtils.equals(typeStr, "getSpeakList"))
		{// ��ȡ˵˵�б�
			getSpeakList(req);
		} else if (TextUtils.equals(typeStr, "getSpeakInfo"))
		{// ��ȡ˵˵����
			getSpeakInfo(req);
		} else if (TextUtils.equals(typeStr, "praisesSpeak"))
		{// ����˵˵
			praisesSpeak(req);
		} else if (TextUtils.equals(typeStr, "unPraisesSpeak"))
		{// ȡ������
			unPraisesSpeak(req);
		} else if (TextUtils.equals(typeStr, "commentSpeak"))
		{// ����
			commentSpeak(req);
		} else if (TextUtils.equals(typeStr, "replySpeak"))
		{ // �ظ�˵˵
			replySpeak(req);
		} else if (TextUtils.equals(typeStr, "addProwseNum"))
		{// ���˵˵�������
			addProwseNum(req);
		} else if (TextUtils.equals(typeStr, "unReplySpeak"))
		{ // ȡ���ظ�˵˵
			unReplySpeak(req);
		} else if (TextUtils.equals(typeStr, "unCommentSpeak"))
		{ // ȡ������˵˵
			unCommentSpeak(req);
		}
	}

	private void addProwseNum(HttpServletRequest req)
	{
		String speakIdSrt = req.getParameter("speakId");
		if (TextUtils.isStringsNulls(speakIdSrt))
		{
			writeOBJ(-5, "��������");
			return;
		}
		OBJ addProwseNum = SpeakDbUtil.getInstance().addProwseNum(speakIdSrt);
		write(JSON.toJSONString(addProwseNum));
	}

	private void getSpeakList(HttpServletRequest req)
	{
		String userId = req.getParameter("userId");
		String pageNumStr = req.getParameter("pageNum");
		String countNumStr = req.getParameter("countNum");
		if (TextUtils.isStringsNulls(userId, pageNumStr, countNumStr))
		{
			writeOBJ(-5, "��������");
			return;
		}
		String userPhone = req.getParameter("userPhone");// ��Ϊnullֵ
		int pageNum = 0;
		int countNum = 0;
		try
		{
			pageNum = Integer.parseInt(pageNumStr);
			countNum = Integer.parseInt(countNumStr);
		} catch (Exception e)
		{
			writeOBJ(-5, "��������");
			return;
		}
		List<Speak> speakList = SpeakDbUtil.getInstance().getSpeakList(pageNum, countNum,
				TextUtils.isStringsNulls(userPhone) ? 0 : UserDbUtil.getInstance().getId(userPhone));
		if (speakList != null)
		{
			write(JSON.toJSONString(new OBJList<Speak>(1, "��ȡ�ɹ�", speakList)));
		} else
		{
			writeOBJ(-2, "��ȡʧ��");
			return;
		}
	}

	private void getSpeakInfo(HttpServletRequest req)
	{
		String speakIdSrt = req.getParameter("speakId");
		if (TextUtils.isStringsNulls(speakIdSrt))
		{
			writeOBJ(-5, "��������");
			return;
		}
		int speakId = 0;
		try
		{
			speakId = Integer.parseInt(speakIdSrt);
		} catch (Exception e)
		{
			writeOBJ(-5, "��������");
			return;
		}
		Speak speakInfo = SpeakDbUtil.getInstance().getSpeakInfo(speakId);
		if (speakInfo != null)
		{
			write(JSON.toJSONString(new OBJList<Speak>(1, "��ȡ�ɹ�", speakInfo)));
		} else
		{
			writeOBJ(-2, "��ȡʧ��");
			return;
		}
	}

	private void praisesSpeak(HttpServletRequest req)
	{
		String speakIdSrt = req.getParameter("speakId");
		String userIdSrt = req.getParameter("userId");
		if (TextUtils.isStringsNulls(speakIdSrt, userIdSrt))
		{
			writeOBJ(-5, "��������");
			return;
		}
		int speakId = Integer.parseInt(speakIdSrt);
		int userId = Integer.parseInt(userIdSrt);
		OBJ addPraises = SpeakDbUtil.getInstance().addPraises(speakId, userId);
		write(JSON.toJSONString(addPraises));
	}

	private void unPraisesSpeak(HttpServletRequest req)
	{
		String praiseIdStr = req.getParameter("praiseId");
		if (TextUtils.isStringsNulls(praiseIdStr))
		{
			writeOBJ(-5, "��������");
			return;
		}
		int praiseId = Integer.parseInt(praiseIdStr);
		OBJ removeunPraises = SpeakDbUtil.getInstance().removeunPraises(praiseId);
		write(JSON.toJSONString(removeunPraises));
	}

	private void commentSpeak(HttpServletRequest req)
	{
		String userIdStr = req.getParameter("userId");
		String speakIdStr = req.getParameter("speakId");
		String contentC = TextUtils.ISO2UTF8(req.getParameter("contentC"));
		if (TextUtils.isStringsNulls(userIdStr, speakIdStr, contentC))
		{
			writeOBJ(-5, "��������");
			return;
		}
		OBJ commentSpeak = SpeakDbUtil.getInstance().commentSpeak(userIdStr, speakIdStr, contentC);
		write(JSON.toJSONString(commentSpeak));
	}

	/**
	 * ȡ������
	 * 
	 * @param req
	 */
	private void unCommentSpeak(HttpServletRequest req)
	{
		String userIdStr = req.getParameter("userId");
		String speakIdStr = req.getParameter("speakId");
		String contentC = TextUtils.ISO2UTF8(req.getParameter("contentC"));
		if (TextUtils.isStringsNulls(userIdStr, speakIdStr, contentC))
		{
			writeOBJ(-5, "��������");
			return;
		}
		OBJ commentSpeak = SpeakDbUtil.getInstance().commentSpeak(userIdStr, speakIdStr, contentC);
		write(JSON.toJSONString(commentSpeak));
	}

	private void replySpeak(HttpServletRequest req)
	{
		String userIdStr = req.getParameter("userId");
		String commentIdStr = req.getParameter("commentId");
		String toUserPhoneR = req.getParameter("toUserPhoneR");
		String contentR = TextUtils.ISO2UTF8(req.getParameter("contentR"));
		if (TextUtils.isStringsNulls(userIdStr, commentIdStr, toUserPhoneR, contentR))
		{
			writeOBJ(-5, "��������");
			return;
		}
		OBJ replySpeak = SpeakDbUtil.getInstance().replySpeak(userIdStr, commentIdStr, toUserPhoneR, contentR);
		write(JSON.toJSONString(replySpeak));
	}

	/**
	 * ȡ���ظ�
	 * 
	 * @param req
	 */
	private void unReplySpeak(HttpServletRequest req)
	{
		String userId = req.getParameter("userId");
		String replyId = req.getParameter("replyId");
		if (TextUtils.isStringsNulls(replyId, userId))
		{
			writeOBJ(-5, "��������");
			return;
		}

		OBJ replySpeak = SpeakDbUtil.getInstance().unReplySpeak(replyId, userId);
		write(JSON.toJSONString(replySpeak));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void publishSpeak(HttpServletRequest req)
	{
		String contentType = req.getContentType();
		// ���ļ��ϴ���Я������
		if (contentType.contains("multipart/form-data") || contentType.contains("multipart/mixed"))
		{
			String savePath = getServletContext().getRealPath("/speakPhoto");
			Map<String, Object> map = UploadUtil.saveSpeakImages(req, savePath);
			List<String> list = new ArrayList<String>();
			list.addAll((List<String>) map.get("list"));
			map.remove("list");
			map.put("_publicTime", System.currentTimeMillis());
			int speakId = SpeakDbUtil.getInstance().publishSpeak(map);
			if (speakId == -1)
			{
				writeOBJ(-2, "����ʧ��");
				return;
			}
			boolean addPhotos = SpeakDbUtil.getInstance().addPhotos(list, speakId + "");
			if (!addPhotos)
			{
				writeOBJ(-2, "����ʧ��");
				return;
			}
			writeOBJ(1, "����ɹ�");
			String nickName = UserDbUtil.getInstance().getNickName(String.valueOf(map.get("_userId")));
			long sendPushAll = new MessagePush(nickName + "������˵˵...����鿴").sendPushAll();
			System.out.println(nickName + "������˵˵...���͸���" + sendPushAll + "��");
			return;
			// ��ͨ�ı��ύ���ϴ�����
		}
		if (contentType.contains("application/x-www-form-urlencoded"))
		{
			Iterator it = req.getParameterMap().entrySet().iterator();
			HashMap<String, Object> map = new HashMap<String, Object>();
			while (it.hasNext())
			{
				Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it.next();
				map.put("_" + entry.getKey().toString(),
						TextUtils.ISO2UTF8(req.getParameter(entry.getKey().toString())));
			}
			map.put("_publicTime", System.currentTimeMillis());
			int speakId = SpeakDbUtil.getInstance().publishSpeak(map);
			if (speakId == -1)
			{
				writeOBJ(-2, "����ʧ��");
				return;
			}
			writeOBJ(1, "����ɹ�");
			return;
		}
		writeOBJ(-5, "��������");
		return;
	}
}