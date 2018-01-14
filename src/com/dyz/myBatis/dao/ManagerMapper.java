package com.dyz.myBatis.dao;

import com.dyz.gameserver.pojo.Manager;

public interface ManagerMapper {
	
	
	/**
	 * 通过传来的码来验证是aa否存在
	 * @param inviteCode
	 * @return
	 */
	Manager selectByInviteCode(String inviteCode);
	

}
