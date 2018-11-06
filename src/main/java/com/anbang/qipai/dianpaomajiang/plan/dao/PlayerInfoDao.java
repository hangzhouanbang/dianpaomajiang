package com.anbang.qipai.dianpaomajiang.plan.dao;

import com.anbang.qipai.dianpaomajiang.plan.bean.PlayerInfo;

public interface PlayerInfoDao {

	PlayerInfo findById(String playerId);

	void save(PlayerInfo playerInfo);
}
