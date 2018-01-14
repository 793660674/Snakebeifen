package com.dyz.gameserver.msg.processor.NearUser;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dyz.gameserver.Avatar;
import com.dyz.gameserver.commons.message.ClientRequest;
import com.dyz.gameserver.commons.session.GameSession;
import com.dyz.gameserver.manager.GameSessionManager;
import com.dyz.gameserver.msg.processor.common.INotAuthProcessor;
import com.dyz.gameserver.msg.processor.common.MsgProcessor;
import com.dyz.gameserver.msg.response.NearUser.NearUserResponse;
import com.dyz.gameserver.pojo.FindUser;
import com.dyz.gameserver.pojo.LanLin;
import com.dyz.gameserver.pojo.NearUser;
import com.dyz.gameserver.pojo.NearUsers;
import com.dyz.myBatis.services.FriendsService;
import com.dyz.persist.util.JsonUtilTool;
import com.dyz.persist.util.TjUtil;
import com.dyz.persist.util.UrlUtil;

import net.sf.json.JSONObject;



/**
 * Created by kevin on 2016/8/2.
 */
public class NearUserMsgProcessor extends MsgProcessor implements INotAuthProcessor {
    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
    	//获取传过来的经纬度
    	String message=request.getString();
    	//从session 中获取个人信息
    	Avatar avatar=gameSession.getRole(Avatar.class);
    	//转成对象
    	LanLin lanLin=JsonUtilTool.fromJson(message,LanLin.class);
    	//如果经纬度不存在 则直接返回
    	if (lanLin.getLatitude().equals("0")) {
			return;
		}
    	//查看lbs 云存储中是否存在此用户
    	String uuid=avatar.getUuId()+"";
    	String content=UrlUtil.GET("http://api.map.baidu.com/geodata/v3/poi/list", "geotable_id=182444&ak=ZfnGskL9wTibnT8cfax0jtlKufLyuDka&coord_type=1&uuid="+uuid+","+uuid);
    	 Map<String,Object>  map= JsonUtilTool.jsonToMap(content);
    	 System.out.println(map.get("total"));
    	 if (Integer.valueOf(map.get("total")+"")==1) {
			//存在 修改
    		String msg="geotable_id=182444&ak=ZfnGskL9wTibnT8cfax0jtlKufLyuDka&latitude="+lanLin.getLatitude()+"&longitude="+lanLin.getLongitude()+"&coord_type=1&uuid="+uuid;
    		String s= UrlUtil.Post("http://api.map.baidu.com/geodata/v3/poi/update",msg);
    		System.out.println(s);
    	 } else {
			//lbs中不存在他的数据 创建
    		 String msg="geotable_id=182444&ak=ZfnGskL9wTibnT8cfax0jtlKufLyuDka&uuid="+uuid+"&latitude="+lanLin.getLatitude()+"&longitude="+lanLin.getLongitude()+"&coord_type=1&nickname="+URLEncoder.encode(avatar.account.getNickname(), "utf-8")+"&url="+avatar.account.getHeadicon();
    		//百度lbs防止乱码的出现 需要将自己的中文汉字转换成utf8  2字符16进制 发送给lbs才可以
    		 System.out.println(URLEncoder.encode(avatar.account.getNickname(), "utf-8"));
    		 String s= UrlUtil.Post("http://api.map.baidu.com/geodata/v3/poi/create", msg);
    		 System.out.println(s);
    	 }
    	 //查询附近的人
    	 String msg=UrlUtil.GET("http://api.map.baidu.com/geosearch/v3/nearby", "geotable_id=182444&ak=ZfnGskL9wTibnT8cfax0jtlKufLyuDka&location&location="+lanLin.getLongitude()+","+lanLin.getLatitude()+"&radius=5000&page_size=20");
    	 System.out.println(msg);
//    	 Map classMap = new HashMap();
//    	 classMap.put("contents",NearUser.class);
//    	 JSONObject jobj = JSONObject.fromObject(msg);
//    	 NearUsers nearUsers =((NearUsers)JSONObject.toBean(jobj, NearUsers.class, classMap));
    	 NearUsers nearUsers =JSON.parseObject(msg, NearUsers.class);
    	 int followeruuid=avatar.getUuId();
    	 List<NearUser> nList=nearUsers.getContents();
    	 if (nearUsers.getTotal()>0) {
			for(NearUser nearUser:nList){
				FindUser finduser=new FindUser();
				finduser.setFolloweruuid(followeruuid);
				finduser.setBefolloweruuid(nearUser.getUuid());
				finduser.setCreatetime(TjUtil.getCurrentDate());
				FindUser findUser=FriendsService.getInstance().selectFindUserByBefolloweruuid(finduser);
				if (findUser!=null) {
					nearUser.setFriend(true);
				}
				if (GameSessionManager.getInstance().getAvatarByUuid(nearUser.getUuid()+"")!=null) {
					nearUser.setIslogin(true);
				}
			}
		}
    	 nearUsers.setContents(nList);
     	 System.out.println(JsonUtilTool.toJson(nearUsers));
    	 gameSession.sendMsg(new NearUserResponse(1, JsonUtilTool.toJson(nearUsers)));
    }
}
