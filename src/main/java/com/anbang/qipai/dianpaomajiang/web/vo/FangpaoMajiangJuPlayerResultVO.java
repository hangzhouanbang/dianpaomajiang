package com.anbang.qipai.dianpaomajiang.web.vo;

import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.DianpaoMajiangJuPlayerResult;
import com.anbang.qipai.dianpaomajiang.cqrs.q.dbo.MajiangGamePlayerDbo;

public class FangpaoMajiangJuPlayerResultVO {

	private String playerId;
	private String nickname;
	private String headimgurl;
	private int huCount;
	private int caishenCount;
	private int zimoCount;
	private int fangPaoCount;
	private int totalScore;

	public FangpaoMajiangJuPlayerResultVO(DianpaoMajiangJuPlayerResult juPlayerResult,
			MajiangGamePlayerDbo majiangGamePlayerDbo) {
		playerId = majiangGamePlayerDbo.getPlayerId();
		nickname = majiangGamePlayerDbo.getNickname();
		headimgurl = majiangGamePlayerDbo.getHeadimgurl();
		huCount = juPlayerResult.getHuCount();
		caishenCount = juPlayerResult.getCaishenCount();
		zimoCount = juPlayerResult.getZimoCount();
		fangPaoCount = juPlayerResult.getFangPaoCount();
		totalScore = juPlayerResult.getTotalScore();
	}

	public FangpaoMajiangJuPlayerResultVO(MajiangGamePlayerDbo majiangGamePlayerDbo) {
		playerId = majiangGamePlayerDbo.getPlayerId();
		nickname = majiangGamePlayerDbo.getNickname();
		headimgurl = majiangGamePlayerDbo.getHeadimgurl();
		huCount = 0;
		caishenCount = 0;
		zimoCount = 0;
		fangPaoCount = 0;
		totalScore = 0;
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

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public int getHuCount() {
		return huCount;
	}

	public void setHuCount(int huCount) {
		this.huCount = huCount;
	}

	public int getCaishenCount() {
		return caishenCount;
	}

	public void setCaishenCount(int caishenCount) {
		this.caishenCount = caishenCount;
	}

	public int getZimoCount() {
		return zimoCount;
	}

	public void setZimoCount(int zimoCount) {
		this.zimoCount = zimoCount;
	}

	public int getFangPaoCount() {
		return fangPaoCount;
	}

	public void setFangPaoCount(int fangPaoCount) {
		this.fangPaoCount = fangPaoCount;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
}
