package com.lin.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lin.model.Comments;
import com.lin.model.OBJ;
import com.lin.model.Praises;
import com.lin.model.Reply;
import com.lin.model.Speak;
import com.lin.model.UserInfo;

/**
 * �û���Ϣ��������
 * 
 * @author huajian.zhang
 * 
 */
public class SpeakDbUtil
{
	private static SpeakDbUtil instance;

	private SpeakDbUtil()
	{
	}

	public static SpeakDbUtil getInstance()
	{
		if (null == instance)
		{
			instance = new SpeakDbUtil();
		}
		return instance;
	}

	public OBJ addProwseNum(String speakId)
	{// �ж��Ƿ���ڸ�˵˵
		if (DbUtil.existNumber("speak", "_speakId = " + speakId) <= 0)
			return new OBJ(-6, "˵˵�����ڣ����ѱ�ɾ��");
		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE speak SET _browseNum = _browseNum + 1 ");
			sb.append(" where _speakId=" + speakId);
			Statement statement = DbUtil.openDb().createStatement();
			int row = statement.executeUpdate(sb.toString());
			if (row > 0)
			{
				return new OBJ(1, "�������ɹ�");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return new OBJ(-2, "������ʧ��");
	}

	public int publishSpeak(Map<String, Object> map)
	{
		if (DbUtil.inserIntoTable(map, "speak"))
		{
			try
			{
				StringBuffer sb = new StringBuffer();
				sb.append("select _speakId ");
				sb.append("from speak ");
				sb.append("where _publicTime= " + map.get("_publicTime").toString());
				PreparedStatement ps = DbUtil.openDb().prepareStatement(sb.toString());
				ResultSet rs = ps.executeQuery();
				while (rs.next())
				{
					int speakId = rs.getInt("_speakId");
					return speakId;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return -1;
	}

	/**
	 * �ظ�˵˵������
	 * 
	 * @param userId
	 *            �ظ��ߵ�ID
	 * @param commentId
	 *            ���۵�ID
	 * @param toUserPhoneR
	 *            �������ߵ��˺�
	 * @param contentR
	 *            �ظ�����
	 * @return
	 */
	public OBJ replySpeak(String userId, String commentId, String toUserPhoneR, String contentR)
	{
		// �ж��Ƿ���ڸ�����
		if (DbUtil.existNumber("comments", "_commentId = " + commentId) <= 0)
			return new OBJ(-6, "���۲����ڣ����ѱ�ɾ��");
		UserInfo userInfo2UserPhone = UserDbUtil.getInstance().getUserInfo2UserPhone(toUserPhoneR);
		if (userInfo2UserPhone == null)
			return new OBJ(-8, "�������û�������");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("_userId", userId);
		map.put("_commentId", commentId);
		map.put("_toUserPhoneR", toUserPhoneR);
		map.put("_toNickNameR", userInfo2UserPhone.getNickName());
		map.put("_contentR", contentR);
		map.put("_timeR", System.currentTimeMillis());
		String nickName = UserDbUtil.getInstance().getNickName(userId + "");// ��ȡ��ǰ�ظ��ߵ��ǳ�
		if (TextUtils.isStringsNulls(nickName) || !DbUtil.inserIntoTable(map, "replys"))
			return new OBJ(-2, "�ظ�ʧ��");
		String tag = getUserId2Comments(commentId);// �������ߵ�ID
		if (!TextUtils.equals(tag, userId + ""))// ����ǵ�ǰ�û����Լ����ۡ��򲻷����κ�����
		{
			new MessagePush(nickName + "�ظ����㣺" + contentR).sendPushTag(tag);
		}
		return new OBJ(1, "�ظ��ɹ�");

	}

	public OBJ unReplySpeak(String replyId, String userId)
	{
		// �ж��Ƿ���ڸ�����
		if (DbUtil.existNumber("replys", "_replyId = " + replyId) <= 0)
			return new OBJ(-3, "�ظ������ڣ����ѱ�ɾ��");
		// �ж��Ƿ���ڸ�����
		if (DbUtil.deleteTable("replys", "_replyId = " + replyId + " and _userId=" + userId))
			return new OBJ(1, "ɾ���ɹ�");
		return new OBJ(-2, "ɾ��ʧ��");
	}

	/**
	 * ����˵˵
	 * 
	 * @param userId
	 *            �����û���ID
	 * @param speakId
	 *            �����۵�˵˵��ID
	 * @param contentC
	 *            ��������
	 * @return
	 */
	public OBJ commentSpeak(String userId, String speakId, String contentC)
	{
		// �ж��Ƿ���ڸ�˵˵
		if (DbUtil.existNumber("speak", "_speakId = " + speakId) <= 0)
			return new OBJ(-6, "˵˵�����ڣ����ѱ�ɾ��");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("_speakId", speakId);
		map.put("_userId", userId);
		map.put("_contentC", contentC);
		map.put("_timeC", System.currentTimeMillis());
		String nickName = UserDbUtil.getInstance().getNickName(userId + "");
		if (TextUtils.isStringsNulls(nickName) || !DbUtil.inserIntoTable(map, "comments"))
			return new OBJ(-2, "����ʧ��");
		String tag = getUserId2Speak(speakId + "");
		if (!TextUtils.equals(tag, userId + ""))// ����ǵ�ǰ�û����Լ����ۡ��򲻷����κ�����
		{
			new MessagePush(nickName + "�������㣺" + contentC).sendPushTag(tag);
		}
		return new OBJ(1, "���۳ɹ�");
	}

	/**
	 * 
	 * ��ӵ���
	 * 
	 * @param speakId
	 *            ˵˵��ID
	 * @param userId
	 *            ˭������
	 * @return
	 */
	public OBJ addPraises(int speakId, int userId)
	{
		// �ж��Ƿ���ڸ�˵˵
		if (DbUtil.existNumber("speak", "_speakId = " + speakId) <= 0)
			return new OBJ(-6, "˵˵�����ڣ����ѱ�ɾ��");
		if (DbUtil.existNumber("praises", "_speakId = " + speakId + " and _userId=" + userId) > 0)
			return new OBJ(-7, "�ѵ���ޣ������ٵ�");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("_speakId", speakId);
		map.put("_userId", userId);
		String nickName = UserDbUtil.getInstance().getNickName(userId + "");
		// ���ͨ���û���ȡ����Ϊ�գ�֤���û�������
		if (TextUtils.isStringsNulls(nickName) || !DbUtil.inserIntoTable(map, "praises"))
		{
			return new OBJ(-2, "����ʧ��");
		}
		String tag = getUserId2Speak(speakId + "");
		if (!TextUtils.equals(tag, userId + ""))// ����ǵ�ǰ�û����Լ����ޡ��򲻷����κ�����
		{
			new MessagePush(nickName + "���������").sendPushTag(tag);
			// new MessagePush(nickName + "���������").sendPushAll();
		}
		return new OBJ(1, "���޳ɹ�");
	}

	/**
	 * ȡ����ȡ����
	 * 
	 * @param speakId
	 *            ˵˵��ID
	 * @return
	 */
	public OBJ removeunPraises(int praiseId)
	{
		if (DbUtil.deleteTable("praises", "_praiseId = " + praiseId))
		{
			return new OBJ(1, "ȡ���ɹ�");
		} else
		{
			return new OBJ(-2, "ȡ��ʧ��");
		}
	}

	/**
	 * ��ȡ˵˵����
	 * 
	 * @param speakId
	 *            ˵˵��ID
	 * @return
	 */
	public Speak getSpeakInfo(int speakId)
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("select ui._icon,ui._nickName,s._publicTime,s._mobileType,s._browseNum,s._content ");
			sb.append("from speak s,userinfo ui ");
			sb.append("where s._speakId= " + speakId);
			sb.append(" and ui._userId=s._userId  ");
			PreparedStatement ps = DbUtil.openDb().prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				String icon = rs.getString("ui._icon");
				String nickName = rs.getString("ui._nickName");
				String publicTime = rs.getString("s._publicTime");
				String content = rs.getString("s._content");
				int browseNum = rs.getInt("s._browseNum");
				String mobileType = rs.getString("s._mobileType");
				return new Speak(speakId, icon, nickName, publicTime, content, browseNum, mobileType,
						getPhotos(speakId), getPraisesList(speakId), getCommentsList(speakId));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ˵˵�б�
	 * 
	 * @param pageNum
	 *            ��ǰҳ��
	 * @param countNum
	 *            ÿҳ����
	 * @return
	 */
	public List<Speak> getSpeakList(int pageNum, int countNum, int userId)
	{
		List<Speak> list = null;
		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("select ui._icon,ui._nickName,s._speakId,s._publicTime,s._mobileType,s._browseNum,s._content ");
			sb.append("from speak s,userinfo ui ");
			sb.append("where ui._userId=s._userId  ");
			if (userId != 0)// ��ȡĳ���˵�����˵˵
				sb.append("and s._userId =  " + userId);
			sb.append(" order by s._publicTime desc");
			sb.append(" limit ");
			sb.append(countNum);
			if (pageNum >= 0)// ҳ��������ڵ���0
			{
				sb.append(" offset ");
				sb.append((pageNum - 1) * countNum);
			}
			PreparedStatement ps = DbUtil.openDb().prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			list = new ArrayList<Speak>();
			while (rs.next())
			{
				String icon = rs.getString("ui._icon");
				String nickName = rs.getString("ui._nickName");
				String publicTime = rs.getString("s._publicTime");
				String content = rs.getString("s._content");
				int browseNum = rs.getInt("s._browseNum");
				String mobileType = rs.getString("s._mobileType");
				int speakId = rs.getInt("s._speakId");
				list.add(new Speak(speakId, icon, nickName, publicTime, content, browseNum, mobileType,
						getPhotos(speakId), getPraisesList(speakId), getCommentsList(speakId)));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ��ȡ����
	 * 
	 * @param speakId
	 *            ˵˵��ID
	 * @return
	 */
	private List<Praises> getPraisesList(int speakId)
	{
		List<Praises> list = null;
		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("select p._praiseId,ui._nickName,ui._userPhone from praises p,userInfo ui ");
			sb.append("where p._speakId=? ");
			sb.append("and ui._userId=p._userId ");
			PreparedStatement ps = DbUtil.openDb().prepareStatement(sb.toString());
			ps.setString(1, speakId + "");
			ResultSet rs = ps.executeQuery();
			list = new ArrayList<Praises>();
			while (rs.next())
			{
				String nickNameP = rs.getString("ui._nickName");
				String userPhoneP = rs.getString("ui._userPhone");
				String praiseId = rs.getString("p._praiseId");
				list.add(new Praises(praiseId, nickNameP, userPhoneP));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ��ȡ�ظ�
	 * 
	 * @param commentId
	 *            ���۵�ID
	 * @return
	 */
	private List<Reply> getReplyList(int commentId)
	{
		List<Reply> list = null;
		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("select r._replyId,r._timeR,r._contentR,r._toNickNameR,r._toUserPhoneR,ui._userPhone,ui._nickName");
			sb.append(" from replys r,userInfo ui ");
			sb.append("where _commentId=? ");
			sb.append("and r._userId=ui._userId ");
			PreparedStatement ps = DbUtil.openDb().prepareStatement(sb.toString());
			ps.setString(1, commentId + "");
			ResultSet rs = ps.executeQuery();
			list = new ArrayList<Reply>();
			while (rs.next())
			{
				String nickNameR = rs.getString("ui._nickName");
				String userPhoneR = rs.getString("ui._userPhone");
				String toNickNameR = rs.getString("r._toNickNameR");
				String toUserPhoneR = rs.getString("r._toUserPhoneR");
				String contentR = rs.getString("r._contentR");
				String timeR = rs.getString("r._timeR");
				String replyId = rs.getString("r._replyId");
				list.add(new Reply(nickNameR, userPhoneR, toNickNameR, toUserPhoneR, contentR, timeR, replyId));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ��ȡ����
	 * 
	 * @param speakId
	 *            ˵˵��ID
	 * @return
	 */
	private List<Comments> getCommentsList(int speakId)
	{
		List<Comments> list = null;
		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("select ui._nickName,ui._userPhone,c._timeC,c._contentC,c._commentId,ui._icon ");
			sb.append("from comments c,userInfo ui ");
			sb.append("where _speakId=? ");
			sb.append("and c._userId = ui._userId ");
			PreparedStatement ps = DbUtil.openDb().prepareStatement(sb.toString());
			ps.setString(1, speakId + "");
			System.out.println("---" + ps.toString());
			ResultSet rs = ps.executeQuery();
			list = new ArrayList<Comments>();
			while (rs.next())
			{
				String nickNameC = rs.getString("ui._nickName");
				String userPhoneC = rs.getString("ui._userPhone");
				String iconC = rs.getString("ui._icon");
				String timeC = rs.getString("c._timeC");
				String contentC = rs.getString("c._contentC");
				int commentId = rs.getInt("c._commentId");
				list.add(new Comments(commentId + "", nickNameC, userPhoneC, iconC, timeC, contentC,
						getReplyList(commentId)));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}

	public boolean addPhotos(List<String> list, String speakId)
	{
		if (list == null || list.size() == 0)
			return false;
		for (String string : list)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("_speakId", speakId);
			map.put("_speakImage", string);
			boolean inserIntoTable = DbUtil.inserIntoTable(map, "speakphotourl");
			if (!inserIntoTable)
				return false;
		}
		return true;
	}

	/**
	 * ��ȡ˵˵��ͼƬ
	 * 
	 * @param speakId
	 *            ˵˵��ID
	 * @return
	 */
	private List<String> getPhotos(int speakId)
	{
		List<String> list = null;
		try
		{
			PreparedStatement ps = DbUtil.openDb().prepareStatement("select * from speakphotourl where _speakId=?");
			ps.setString(1, speakId + "");
			ResultSet rs = ps.executeQuery();
			list = new ArrayList<String>();
			while (rs.next())
			{
				String speakImage = rs.getString("_speakImage");
				list.add(DbUtil.HTTP_ADDRESS + speakImage);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}

	private String getUserId2Speak(String speakId)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("select _userId ");
		sb.append("from speak ");
		sb.append("where _speakId= " + speakId);
		try
		{
			PreparedStatement ps = DbUtil.openDb().prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				return rs.getString("_userId");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private String getUserId2Comments(String _commentId)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("select _userId ");
		sb.append("from comments ");
		sb.append("where _commentId= " + _commentId);
		try
		{
			PreparedStatement ps = DbUtil.openDb().prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				return rs.getString("_userId");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
