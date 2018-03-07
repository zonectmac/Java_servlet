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
 * 用户信息操作工具
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
	{// 判断是否存在该说说
		if (DbUtil.existNumber("speak", "_speakId = " + speakId) <= 0)
			return new OBJ(-6, "说说不存在，或已被删除");
		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE speak SET _browseNum = _browseNum + 1 ");
			sb.append(" where _speakId=" + speakId);
			Statement statement = DbUtil.openDb().createStatement();
			int row = statement.executeUpdate(sb.toString());
			if (row > 0)
			{
				return new OBJ(1, "添加浏览成功");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return new OBJ(-2, "添加浏览失败");
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
	 * 回复说说的评论
	 * 
	 * @param userId
	 *            回复者的ID
	 * @param commentId
	 *            评论的ID
	 * @param toUserPhoneR
	 *            被评论者的账号
	 * @param contentR
	 *            回复内容
	 * @return
	 */
	public OBJ replySpeak(String userId, String commentId, String toUserPhoneR, String contentR)
	{
		// 判断是否存在该评论
		if (DbUtil.existNumber("comments", "_commentId = " + commentId) <= 0)
			return new OBJ(-6, "评论不存在，或已被删除");
		UserInfo userInfo2UserPhone = UserDbUtil.getInstance().getUserInfo2UserPhone(toUserPhoneR);
		if (userInfo2UserPhone == null)
			return new OBJ(-8, "被评论用户不存在");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("_userId", userId);
		map.put("_commentId", commentId);
		map.put("_toUserPhoneR", toUserPhoneR);
		map.put("_toNickNameR", userInfo2UserPhone.getNickName());
		map.put("_contentR", contentR);
		map.put("_timeR", System.currentTimeMillis());
		String nickName = UserDbUtil.getInstance().getNickName(userId + "");// 获取当前回复者的昵称
		if (TextUtils.isStringsNulls(nickName) || !DbUtil.inserIntoTable(map, "replys"))
			return new OBJ(-2, "回复失败");
		String tag = getUserId2Comments(commentId);// 被评论者的ID
		if (!TextUtils.equals(tag, userId + ""))// 如果是当前用户给自己评论。则不发送任何推送
		{
			new MessagePush(nickName + "回复了你：" + contentR).sendPushTag(tag);
		}
		return new OBJ(1, "回复成功");

	}

	public OBJ unReplySpeak(String replyId, String userId)
	{
		// 判断是否存在该评论
		if (DbUtil.existNumber("replys", "_replyId = " + replyId) <= 0)
			return new OBJ(-3, "回复不存在，或已被删除");
		// 判断是否存在该评论
		if (DbUtil.deleteTable("replys", "_replyId = " + replyId + " and _userId=" + userId))
			return new OBJ(1, "删除成功");
		return new OBJ(-2, "删除失败");
	}

	/**
	 * 评论说说
	 * 
	 * @param userId
	 *            评论用户的ID
	 * @param speakId
	 *            被评论的说说的ID
	 * @param contentC
	 *            评论内容
	 * @return
	 */
	public OBJ commentSpeak(String userId, String speakId, String contentC)
	{
		// 判断是否存在该说说
		if (DbUtil.existNumber("speak", "_speakId = " + speakId) <= 0)
			return new OBJ(-6, "说说不存在，或已被删除");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("_speakId", speakId);
		map.put("_userId", userId);
		map.put("_contentC", contentC);
		map.put("_timeC", System.currentTimeMillis());
		String nickName = UserDbUtil.getInstance().getNickName(userId + "");
		if (TextUtils.isStringsNulls(nickName) || !DbUtil.inserIntoTable(map, "comments"))
			return new OBJ(-2, "评论失败");
		String tag = getUserId2Speak(speakId + "");
		if (!TextUtils.equals(tag, userId + ""))// 如果是当前用户给自己评论。则不发送任何推送
		{
			new MessagePush(nickName + "评论了你：" + contentC).sendPushTag(tag);
		}
		return new OBJ(1, "评论成功");
	}

	/**
	 * 
	 * 添加点赞
	 * 
	 * @param speakId
	 *            说说的ID
	 * @param userId
	 *            谁点了赞
	 * @return
	 */
	public OBJ addPraises(int speakId, int userId)
	{
		// 判断是否存在该说说
		if (DbUtil.existNumber("speak", "_speakId = " + speakId) <= 0)
			return new OBJ(-6, "说说不存在，或已被删除");
		if (DbUtil.existNumber("praises", "_speakId = " + speakId + " and _userId=" + userId) > 0)
			return new OBJ(-7, "已点过赞，不可再点");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("_speakId", speakId);
		map.put("_userId", userId);
		String nickName = UserDbUtil.getInstance().getNickName(userId + "");
		// 如果通过用户获取名字为空，证明用户不存在
		if (TextUtils.isStringsNulls(nickName) || !DbUtil.inserIntoTable(map, "praises"))
		{
			return new OBJ(-2, "点赞失败");
		}
		String tag = getUserId2Speak(speakId + "");
		if (!TextUtils.equals(tag, userId + ""))// 如果是当前用户给自己点赞。则不发送任何推送
		{
			new MessagePush(nickName + "给你点了赞").sendPushTag(tag);
			// new MessagePush(nickName + "给你点了赞").sendPushAll();
		}
		return new OBJ(1, "点赞成功");
	}

	/**
	 * 取消获取点赞
	 * 
	 * @param speakId
	 *            说说的ID
	 * @return
	 */
	public OBJ removeunPraises(int praiseId)
	{
		if (DbUtil.deleteTable("praises", "_praiseId = " + praiseId))
		{
			return new OBJ(1, "取消成功");
		} else
		{
			return new OBJ(-2, "取消失败");
		}
	}

	/**
	 * 获取说说详情
	 * 
	 * @param speakId
	 *            说说的ID
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
	 * 获取说说列表
	 * 
	 * @param pageNum
	 *            当前页码
	 * @param countNum
	 *            每页几条
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
			if (userId != 0)// 获取某个人的所有说说
				sb.append("and s._userId =  " + userId);
			sb.append(" order by s._publicTime desc");
			sb.append(" limit ");
			sb.append(countNum);
			if (pageNum >= 0)// 页数必须大于等于0
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
	 * 获取点赞
	 * 
	 * @param speakId
	 *            说说的ID
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
	 * 获取回复
	 * 
	 * @param commentId
	 *            评论的ID
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
	 * 获取评论
	 * 
	 * @param speakId
	 *            说说的ID
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
	 * 获取说说的图片
	 * 
	 * @param speakId
	 *            说说的ID
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
