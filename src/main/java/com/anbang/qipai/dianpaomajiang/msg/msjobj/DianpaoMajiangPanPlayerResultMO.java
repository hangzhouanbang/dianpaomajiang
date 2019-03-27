package com.anbang.qipai.dianpaomajiang.msg.msjobj;

import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.DianpaoMajiangPanPlayerResultDbo;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.MajiangGamePlayerDbo;

public class DianpaoMajiangPanPlayerResultMO {
	private String playerId;// 玩家id
	private String nickname;// 玩家昵称
	private int score;// 一盘总分
	private boolean hu;

	public DianpaoMajiangPanPlayerResultMO(MajiangGamePlayerDbo gamePlayerDbo,
			DianpaoMajiangPanPlayerResultDbo panPlayerResult) {
		playerId = gamePlayerDbo.getPlayerId();
		nickname = gamePlayerDbo.getNickname();
		score = panPlayerResult.getPlayerResult().getScore();
		hu = panPlayerResult.getPlayer().getHu() != null;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

}
