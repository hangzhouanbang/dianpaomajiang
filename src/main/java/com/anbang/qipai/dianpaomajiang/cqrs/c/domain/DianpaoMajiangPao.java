package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import com.dml.majiang.player.MajiangPlayer;

public class DianpaoMajiangPao {
	private int caishenShu;
	private int totalscore;// 总得分
	private int value;// 个人结算分

	public DianpaoMajiangPao() {

	}

	public DianpaoMajiangPao(MajiangPlayer player) {
		caishenShu = player.countGuipai();
	}

	public void calculate(boolean dapao, int playerCount) {
		int pao = 0;
		if (dapao) {
			pao = caishenShu;
			if (pao == 3) {
				pao = 2 * pao;
			}
		}
		value = pao;
		totalscore = pao * (playerCount - 1);
	}

	public int jiesuan(int delta) {
		return totalscore += delta;
	}

	public int getCaishenShu() {
		return caishenShu;
	}

	public void setCaishenShu(int caishenShu) {
		this.caishenShu = caishenShu;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getTotalscore() {
		return totalscore;
	}

	public void setTotalscore(int totalscore) {
		this.totalscore = totalscore;
	}
}
