package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

/**
 * 结算规则 自摸：所有人跟胡家结算胡牌牌型分数；杠分单独结算 放炮：其他玩家不用与胡家结算，只有放炮的人与胡家结算胡牌牌型分数；杠分单独结算
 * 
 * @author lsc
 *
 */
public class DianpaoMajiangHufen {
	private boolean hu;// 普通放炮胡
	private boolean zimoHu;// 自摸胡；
	private boolean qiangganghu;// 抢杠胡；
	private boolean pengpenghu;// 碰碰胡；
	private boolean gangshangkaihua;// 杠上开花；
	private boolean danzhangdiao;// 单张吊；
	private boolean sancaishen;// 三财神有胡型；
	private boolean wuhuxing;// 三财神无胡型；
	private boolean tianhu;// 天胡；
	private boolean dihu;// 地胡；
	private int value;

	public void calculate() {
		int hushu = 0;
		if (hu) {
			hushu = 1;
		}
		if (zimoHu) {
			hushu = 2;
		}
		if (gangshangkaihua) {
			hushu = 2;
		}
		if (pengpenghu && !zimoHu) {
			hushu = 2;
		}
		if (qiangganghu) {
			hushu = 3;
		}
		if (wuhuxing) {
			hushu = 3;
		}
		if (pengpenghu && zimoHu) {
			hushu = 4;
		}
		if (danzhangdiao && !zimoHu) {
			hushu = 4;
		}
		if (danzhangdiao && zimoHu) {
			hushu = 6;
		}
		if (sancaishen && zimoHu) {
			hushu = 6;
		}
		if (tianhu) {
			hushu = 6;
		}
		if (dihu) {
			hushu = 6;
		}
		if (pengpenghu && gangshangkaihua) {
			hushu = 6;
		}
		if (danzhangdiao && gangshangkaihua) {
			hushu = 8;
		}
		if (sancaishen && pengpenghu && zimoHu) {
			hushu = 8;
		}
		if (sancaishen && gangshangkaihua && zimoHu) {
			hushu = 10;
		}
		value = hushu;
	}

	public int jiesuan(int delta) {
		return value += delta;
	}

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

	public boolean isZimoHu() {
		return zimoHu;
	}

	public void setZimoHu(boolean zimoHu) {
		this.zimoHu = zimoHu;
	}

	public boolean isQiangganghu() {
		return qiangganghu;
	}

	public void setQiangganghu(boolean qiangganghu) {
		this.qiangganghu = qiangganghu;
	}

	public boolean isPengpenghu() {
		return pengpenghu;
	}

	public void setPengpenghu(boolean pengpenghu) {
		this.pengpenghu = pengpenghu;
	}

	public boolean isGangshangkaihua() {
		return gangshangkaihua;
	}

	public void setGangshangkaihua(boolean gangshangkaihua) {
		this.gangshangkaihua = gangshangkaihua;
	}

	public boolean isDanzhangdiao() {
		return danzhangdiao;
	}

	public void setDanzhangdiao(boolean danzhangdiao) {
		this.danzhangdiao = danzhangdiao;
	}

	public boolean isSancaishen() {
		return sancaishen;
	}

	public void setSancaishen(boolean sancaishen) {
		this.sancaishen = sancaishen;
	}

	public boolean isWuhuxing() {
		return wuhuxing;
	}

	public void setWuhuxing(boolean wuhuxing) {
		this.wuhuxing = wuhuxing;
	}

	public boolean isTianhu() {
		return tianhu;
	}

	public void setTianhu(boolean tianhu) {
		this.tianhu = tianhu;
	}

	public boolean isDihu() {
		return dihu;
	}

	public void setDihu(boolean dihu) {
		this.dihu = dihu;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
