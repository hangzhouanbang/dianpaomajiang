package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.dml.majiang.pai.MajiangPai;

public class DianpaoMajiangNiao {
	private List<MajiangPai> zhuaPai;
	private List<MajiangPai> niaoPai;
	private int totalScore;
	private int value;

	public DianpaoMajiangNiao() {

	}

	public DianpaoMajiangNiao(boolean quzhongfabai, boolean zhuaniao, int niaoshu) {
		zhuaPai = new ArrayList<>();
		if (zhuaniao) {
			Set<MajiangPai> notPlaySet = new HashSet<>();
			notPlaySet.add(MajiangPai.chun);
			notPlaySet.add(MajiangPai.xia);
			notPlaySet.add(MajiangPai.qiu);
			notPlaySet.add(MajiangPai.dong);
			notPlaySet.add(MajiangPai.mei);
			notPlaySet.add(MajiangPai.lan);
			notPlaySet.add(MajiangPai.zhu);
			notPlaySet.add(MajiangPai.ju);
			if (quzhongfabai) {
				notPlaySet.add(MajiangPai.facai);
				notPlaySet.add(MajiangPai.baiban);
				notPlaySet.add(MajiangPai.hongzhong);
			}
			MajiangPai[] allMajiangPaiArray = MajiangPai.values();
			List<MajiangPai> playPaiTypeList = new ArrayList<>();
			for (int i = 0; i < allMajiangPaiArray.length; i++) {
				MajiangPai pai = allMajiangPaiArray[i];
				if (!notPlaySet.contains(pai)) {
					playPaiTypeList.add(pai);
				}
			}

			List<MajiangPai> allPaiList = new ArrayList<>();
			playPaiTypeList.forEach((paiType) -> {
				for (int i = 0; i < 4; i++) {
					allPaiList.add(paiType);
				}
			});
			Random r = new Random();
			for (int i = 0; i < niaoshu; i++) {
				int index = r.nextInt(allPaiList.size());
				MajiangPai majiangPai = allPaiList.remove(index);
				zhuaPai.add(majiangPai);
			}
		}
	}

	public void calculate() {
		int niao = 0;
		niaoPai = new ArrayList<>();
		niaoPai.add(MajiangPai.yitiao);
		niaoPai.add(MajiangPai.yiwan);
		niaoPai.add(MajiangPai.yitong);
		niaoPai.add(MajiangPai.wutiao);
		niaoPai.add(MajiangPai.wuwan);
		niaoPai.add(MajiangPai.wutong);
		niaoPai.add(MajiangPai.jiutiao);
		niaoPai.add(MajiangPai.jiuwan);
		niaoPai.add(MajiangPai.jiutong);
		for (MajiangPai pai : zhuaPai) {
			if (niaoPai.contains(pai)) {
				niao++;
			}
		}
		value = niao;
	}

	public int jiesuan(int delta) {
		return totalScore += delta;
	}

	public List<MajiangPai> getZhuaPai() {
		return zhuaPai;
	}

	public void setZhuaPai(List<MajiangPai> zhuaPai) {
		this.zhuaPai = zhuaPai;
	}

	public List<MajiangPai> getNiaoPai() {
		return niaoPai;
	}

	public void setNiaoPai(List<MajiangPai> niaoPai) {
		this.niaoPai = niaoPai;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
