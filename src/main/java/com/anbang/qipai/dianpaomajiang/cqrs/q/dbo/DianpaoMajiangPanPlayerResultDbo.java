package com.anbang.qipai.dianpaomajiang.cqrs.q.dbo;

import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.DianpaoMajiangPanPlayerResult;
import com.dml.majiang.player.valueobj.MajiangPlayerValueObject;

public class DianpaoMajiangPanPlayerResultDbo {
	private String playerId;
	private DianpaoMajiangPanPlayerResult playerResult;
	private MajiangPlayerValueObject player;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public DianpaoMajiangPanPlayerResult getPlayerResult() {
		return playerResult;
	}

	public void setPlayerResult(DianpaoMajiangPanPlayerResult playerResult) {
		this.playerResult = playerResult;
	}

	public MajiangPlayerValueObject getPlayer() {
		return player;
	}

	public void setPlayer(MajiangPlayerValueObject player) {
		this.player = player;
	}

}
