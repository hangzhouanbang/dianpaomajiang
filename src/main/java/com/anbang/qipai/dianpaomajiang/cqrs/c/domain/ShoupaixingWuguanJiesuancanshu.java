package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import com.dml.majiang.player.MajiangPlayer;

/**
 * 手牌型无关结算参数
 * 
 * @author lsc
 *
 */
public class ShoupaixingWuguanJiesuancanshu {
	private int caishenShu;
	private int chichupaiZuCount;
	private int fangruShoupaiCount;

	public ShoupaixingWuguanJiesuancanshu(MajiangPlayer player) {
		caishenShu = player.countGuipai();
		chichupaiZuCount = player.countChichupaiZu();
		fangruShoupaiCount = player.getFangruShoupaiList().size();
	}

	public int getChichupaiZuCount() {
		return chichupaiZuCount;
	}

	public void setChichupaiZuCount(int chichupaiZuCount) {
		this.chichupaiZuCount = chichupaiZuCount;
	}

	public int getCaishenShu() {
		return caishenShu;
	}

	public void setCaishenShu(int caishenShu) {
		this.caishenShu = caishenShu;
	}

	public int getFangruShoupaiCount() {
		return fangruShoupaiCount;
	}

	public void setFangruShoupaiCount(int fangruShoupaiCount) {
		this.fangruShoupaiCount = fangruShoupaiCount;
	}
}
