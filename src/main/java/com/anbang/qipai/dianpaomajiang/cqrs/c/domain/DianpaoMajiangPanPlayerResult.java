package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import com.dml.majiang.pan.result.PanPlayerResult;

public class DianpaoMajiangPanPlayerResult extends PanPlayerResult {

	private DianpaoMajiangHufen hufen;

	private DianpaoMajiangGang gang;

	private DianpaoMajiangPao pao;

	private DianpaoMajiangNiao niao;

	private int score;// 一盘的结算分

	/**
	 * 可能为负数
	 */
	private int totalScore;

	public DianpaoMajiangHufen getHufen() {
		return hufen;
	}

	public void setHufen(DianpaoMajiangHufen hufen) {
		this.hufen = hufen;
	}

	public DianpaoMajiangGang getGang() {
		return gang;
	}

	public void setGang(DianpaoMajiangGang gang) {
		this.gang = gang;
	}

	public DianpaoMajiangPao getPao() {
		return pao;
	}

	public void setPao(DianpaoMajiangPao pao) {
		this.pao = pao;
	}

	public DianpaoMajiangNiao getNiao() {
		return niao;
	}

	public void setNiao(DianpaoMajiangNiao niao) {
		this.niao = niao;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
