package com.dyz.gameserver.msg.processor.chongzhi;

import com.dyz.gameserver.Avatar;
import com.dyz.gameserver.commons.message.ClientRequest;
import com.dyz.gameserver.commons.session.GameSession;
import com.dyz.gameserver.manager.GameSessionManager;
import com.dyz.gameserver.msg.processor.common.INotAuthProcessor;
import com.dyz.gameserver.msg.processor.common.MsgProcessor;
import com.dyz.gameserver.msg.response.chongzhi.ChongZhiResponse;
import com.dyz.gameserver.pojo.Account;

public class ChongZhiMsgProcessor extends MsgProcessor implements INotAuthProcessor{

	@Override
	public void process(GameSession gameSession, ClientRequest request) throws Exception {
		String msg=request.getString();
		String []msgs=msg.split(":");
		int uuid=Integer.valueOf(msgs[0]);
		GameSession gameSession2=GameSessionManager.getInstance().getAvatarByUuid(uuid+"");
		System.out.println(msgs[1]+"充值");
		Avatar avatar=gameSession2.getRole(Avatar.class);
		avatar.account.setMoney(avatar.account.getMoney()+Integer.valueOf(msgs[1]));
		gameSession2.sendMsg(new ChongZhiResponse(1, avatar.account.getMoney()+""));
		System.out.println(msgs[1]+"充值");
		/*ssGameSession gameSession2=GameSessionManager.getInstance().getAvatarByUuid(uuid+"");*/
		gameSession.sendMsg(new ChongZhiResponse(1, msgs[1]));
	}
	
}
