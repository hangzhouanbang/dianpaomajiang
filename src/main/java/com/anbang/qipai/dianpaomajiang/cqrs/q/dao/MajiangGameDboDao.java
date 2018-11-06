package com.anbang.qipai.dianpaomajiang.cqrs.q.dao;

import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.MajiangGameDbo;
import com.dml.mpgame.game.player.GamePlayerOnlineState;

public interface MajiangGameDboDao {

	MajiangGameDbo findById(String id);

	void save(MajiangGameDbo majiangGameDbo);

	void updatePlayerOnlineState(String id, String playerId, GamePlayerOnlineState onlineState);

}
