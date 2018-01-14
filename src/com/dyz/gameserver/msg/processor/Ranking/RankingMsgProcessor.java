package com.dyz.gameserver.msg.processor.Ranking;


import com.dyz.gameserver.commons.message.ClientRequest;
import com.dyz.gameserver.commons.session.GameSession;
import com.dyz.gameserver.msg.processor.common.INotAuthProcessor;
import com.dyz.gameserver.msg.processor.common.MsgProcessor;
import com.dyz.gameserver.msg.response.Ranking.RankingResponse;
import com.dyz.gameserver.pojo.PageModel;
import com.dyz.myBatis.services.AccountService;



/**
 * Created by kevin on 2016/8/2.
 */
public class RankingMsgProcessor extends MsgProcessor implements INotAuthProcessor {
    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
    	PageModel pageModel=new PageModel();
    	pageModel.setCurrentPageDatas(AccountService.getInstance().selectTopAccounts());
    	gameSession.sendMsg(new RankingResponse(1, pageModel));
    	
    	
    }
}
