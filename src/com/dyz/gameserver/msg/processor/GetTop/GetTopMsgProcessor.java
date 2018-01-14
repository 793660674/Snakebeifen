package com.dyz.gameserver.msg.processor.GetTop;



import java.util.List;

import com.dyz.gameserver.Avatar;
import com.dyz.gameserver.commons.message.ClientRequest;
import com.dyz.gameserver.commons.session.GameSession;
import com.dyz.gameserver.manager.GameSessionManager;
import com.dyz.gameserver.msg.processor.common.INotAuthProcessor;
import com.dyz.gameserver.msg.processor.common.MsgProcessor;
import com.dyz.gameserver.msg.response.GetTop.GetTopResponse;
import com.dyz.gameserver.pojo.Account;
import com.dyz.gameserver.pojo.FindUser;
import com.dyz.myBatis.services.AccountService;
import com.dyz.myBatis.services.FriendsService;
import com.dyz.persist.util.TjUtil;

import net.sf.json.JSONObject;

public class GetTopMsgProcessor extends MsgProcessor implements INotAuthProcessor{

	@Override
	public  void process(GameSession gameSession, ClientRequest request)
			throws Exception {
		List<Account> yestadayChramnun=AccountService.getInstance().selectYesterdayCharmnumTopAccounts();
		List<Account> todayChramnun=AccountService.getInstance().selectToDayCharmnumTopAccounts();
		JSONObject jsonObject=new JSONObject();
		int followeruuid=gameSession.getRole(Avatar.class).getUuId();
		for (Account array:yestadayChramnun) {
			FindUser finduser=new FindUser();
			finduser.setFolloweruuid(followeruuid);
			finduser.setBefolloweruuid(array.getUuid());
			finduser.setCreatetime(TjUtil.getCurrentDate());
			FindUser findUser=FriendsService.getInstance().selectFindUserByBefolloweruuid(finduser);
			if (findUser!=null) {
				array.setIsfirend(true);
			}
			if (GameSessionManager.getInstance().getAvatarByUuid(array.getUuid()+"")!=null) {
				array.setIslogin(true);
			}
		}
		for (Account array:todayChramnun) {
			FindUser finduser=new FindUser();
			finduser.setFolloweruuid(followeruuid);
			finduser.setBefolloweruuid(array.getUuid());
			finduser.setCreatetime(TjUtil.getCurrentDate());
			FindUser findUser=FriendsService.getInstance().selectFindUserByBefolloweruuid(finduser);
			if (findUser!=null) {
				array.setIsfirend(true);
			}
			if (GameSessionManager.getInstance().getAvatarByUuid(array.getUuid()+"")!=null) {
				array.setIslogin(true);
			}
		}
		jsonObject.put("ToDayCharmnumTopAccounts", todayChramnun);
		jsonObject.put("YesterdayCharmnumTopAccounts", yestadayChramnun);
		gameSession.sendMsg(new GetTopResponse(1, jsonObject.toString()));
	}

}
