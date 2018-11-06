package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.ju.result.JuResult;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGameValueObject;

public class MajiangGameValueObject extends FixedPlayersMultipanAndVotetofinishGameValueObject {

	private int panshu;
	private int renshu;
	private boolean dianpao;
	private boolean dapao;
	private boolean quzhongfa;
	private boolean zhuaniao;
	private Map<String, Integer> playeTotalScoreMap = new HashMap<>();
	private JuResult juResult;

	public MajiangGameValueObject(MajiangGame majiangGame) {
		super(majiangGame);
		panshu = majiangGame.getPanshu();
		renshu = majiangGame.getRenshu();
		dianpao = majiangGame.isDianpao();
		dapao = majiangGame.isDapao();
		quzhongfa = majiangGame.isQuzhongfa();
		zhuaniao = majiangGame.isZhuaniao();
		playeTotalScoreMap.putAll(majiangGame.getPlayeTotalScoreMap());
		if (majiangGame.getJu() != null) {
			juResult = majiangGame.getJu().getJuResult();
		}
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public boolean isDianpao() {
		return dianpao;
	}

	public void setDianpao(boolean dianpao) {
		this.dianpao = dianpao;
	}

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

	public boolean isQuzhongfa() {
		return quzhongfa;
	}

	public void setQuzhongfa(boolean quzhongfa) {
		this.quzhongfa = quzhongfa;
	}

	public boolean isZhuaniao() {
		return zhuaniao;
	}

	public void setZhuaniao(boolean zhuaniao) {
		this.zhuaniao = zhuaniao;
	}

	public Map<String, Integer> getPlayeTotalScoreMap() {
		return playeTotalScoreMap;
	}

	public void setPlayeTotalScoreMap(Map<String, Integer> playeTotalScoreMap) {
		this.playeTotalScoreMap = playeTotalScoreMap;
	}

	public JuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(JuResult juResult) {
		this.juResult = juResult;
	}

}
