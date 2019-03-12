package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import com.dml.majiang.player.MajiangPlayer;

/**
 * 手牌型无关结算参数
 * 
 * @author lsc
 *
 */
public class ShoupaixingWuguanJiesuancanshu {
	private boolean allXushupaiInSameCategory;
	private boolean hasZipai;
	private boolean qingyise;
	private boolean hunyise;
	private int caishenShu;
	private int chichupaiZuCount;
	private int fangruShoupaiCount;

	public ShoupaixingWuguanJiesuancanshu(MajiangPlayer player) {
		allXushupaiInSameCategory = player.allXushupaiInSameCategory();
		hasZipai = player.hasZipai();
		qingyise = (allXushupaiInSameCategory && !hasZipai);
		hunyise = (allXushupaiInSameCategory && hasZipai);
		caishenShu = player.countGuipai();
		chichupaiZuCount = player.countChichupaiZu();
		fangruShoupaiCount = player.countAllFangruShoupai();
	}

	public boolean isHunyise() {
		return hunyise;
	}

	public void setHunyise(boolean hunyise) {
		this.hunyise = hunyise;
	}

	public boolean isQingyise() {
		return qingyise;
	}

	public void setQingyise(boolean qingyise) {
		this.qingyise = qingyise;
	}

	public boolean isAllXushupaiInSameCategory() {
		return allXushupaiInSameCategory;
	}

	public void setAllXushupaiInSameCategory(boolean allXushupaiInSameCategory) {
		this.allXushupaiInSameCategory = allXushupaiInSameCategory;
	}

	public boolean isHasZipai() {
		return hasZipai;
	}

	public void setHasZipai(boolean hasZipai) {
		this.hasZipai = hasZipai;
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
