package com.anbang.qipai.dianpaomajiang.web.vo;

import com.anbang.qipai.dianpaomajiang.cqrs.c.domain.DianpaoMajiangHufen;

public class DianpaoMajiangHufenVO {
	private boolean gangshangkaihua;// 杠上开花
	private boolean qiangganghu;// 抢杠胡
	private boolean qingyise;// 清一色
	private boolean hunyise;// 混一色
	private boolean wuhuxing;// 三财神无胡型
	private boolean pengpenghu;// 碰碰胡
	private boolean danzhangdiao;// 单张吊
	private boolean tianhu;// 天胡
	private boolean dihu;// 地胡
	private boolean sancaishen;// 三财神有胡型
	private boolean ppgk;// 杠上开花碰碰胡
	private boolean dzgk;// 杠上开花单张吊
	private boolean scspp;// 三财神碰碰胡
	private boolean scsgk;// 三财神杠开胡

	public DianpaoMajiangHufenVO() {

	}

	public DianpaoMajiangHufenVO(DianpaoMajiangHufen hufen) {
		if (hufen.isSancaishen() && hufen.isGangshangkaihua() && hufen.isZimoHu()) {
			scsgk = true;
		} else if (hufen.isSancaishen() && hufen.isPengpenghu() && hufen.isZimoHu()) {
			scspp = true;
		} else if (hufen.isGangshangkaihua() && hufen.isDanzhangdiao()) {
			dzgk = true;
		} else if (hufen.isGangshangkaihua() && hufen.isPengpenghu()) {
			ppgk = true;
		} else if (hufen.isDihu()) {
			dihu = true;
		} else if (hufen.isTianhu()) {
			tianhu = true;
		} else if (hufen.isQingyise()) {
			qingyise = true;
		} else if (hufen.isSancaishen() && hufen.isZimoHu()) {
			sancaishen = true;
		} else if (hufen.isDanzhangdiao()) {
			danzhangdiao = true;
		} else if (hufen.isPengpenghu() && hufen.isZimoHu()) {
			pengpenghu = true;
		} else if (hufen.isWuhuxing()) {
			wuhuxing = true;
		} else if (hufen.isHunyise()) {
			hunyise = true;
		} else if (hufen.isPengpenghu()) {
			pengpenghu = true;
		} else if (hufen.isQiangganghu()) {
			qiangganghu = true;
		} else if (hufen.isPengpenghu() && !hufen.isZimoHu()) {
			pengpenghu = true;
		} else if (hufen.isGangshangkaihua()) {
			gangshangkaihua = true;
		}
	}

	public boolean isHunyise() {
		return hunyise;
	}

	public void setHunyise(boolean hunyise) {
		this.hunyise = hunyise;
	}

	public boolean isGangshangkaihua() {
		return gangshangkaihua;
	}

	public void setGangshangkaihua(boolean gangshangkaihua) {
		this.gangshangkaihua = gangshangkaihua;
	}

	public boolean isQiangganghu() {
		return qiangganghu;
	}

	public void setQiangganghu(boolean qiangganghu) {
		this.qiangganghu = qiangganghu;
	}

	public boolean isWuhuxing() {
		return wuhuxing;
	}

	public void setWuhuxing(boolean wuhuxing) {
		this.wuhuxing = wuhuxing;
	}

	public boolean isPengpenghu() {
		return pengpenghu;
	}

	public void setPengpenghu(boolean pengpenghu) {
		this.pengpenghu = pengpenghu;
	}

	public boolean isDanzhangdiao() {
		return danzhangdiao;
	}

	public void setDanzhangdiao(boolean danzhangdiao) {
		this.danzhangdiao = danzhangdiao;
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

	public boolean isSancaishen() {
		return sancaishen;
	}

	public void setSancaishen(boolean sancaishen) {
		this.sancaishen = sancaishen;
	}

	public boolean isPpgk() {
		return ppgk;
	}

	public void setPpgk(boolean ppgk) {
		this.ppgk = ppgk;
	}

	public boolean isDzgk() {
		return dzgk;
	}

	public void setDzgk(boolean dzgk) {
		this.dzgk = dzgk;
	}

	public boolean isScspp() {
		return scspp;
	}

	public void setScspp(boolean scspp) {
		this.scspp = scspp;
	}

	public boolean isScsgk() {
		return scsgk;
	}

	public void setScsgk(boolean scsgk) {
		this.scsgk = scsgk;
	}

	public boolean isQingyise() {
		return qingyise;
	}

	public void setQingyise(boolean qingyise) {
		this.qingyise = qingyise;
	}

}
