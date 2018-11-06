package com.anbang.qipai.dianpaomajiang.msg.msjobj;

import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.DianpaoMajiangPanPlayerResultDbo;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.MajiangGamePlayerDbo;

public class FangpaoMajiangPanPlayerResultMO {
	private String playerId;// 玩家id
	private String nickname;// 玩家昵称
	private int score;// 一盘总分

	public FangpaoMajiangPanPlayerResultMO(MajiangGamePlayerDbo gamePlayerDbo,
			DianpaoMajiangPanPlayerResultDbo panPlayerResult) {
		playerId = gamePlayerDbo.getPlayerId();
		nickname = gamePlayerDbo.getNickname();
		score = panPlayerResult.getPlayerResult().getScore();
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

}
