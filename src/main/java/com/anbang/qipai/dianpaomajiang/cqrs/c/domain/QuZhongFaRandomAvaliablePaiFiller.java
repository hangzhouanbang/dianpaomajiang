package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.avaliablepai.AvaliablePaiFiller;

public class QuZhongFaRandomAvaliablePaiFiller implements AvaliablePaiFiller {

	private long seed;

	private boolean quzhongfa;

	public QuZhongFaRandomAvaliablePaiFiller() {
	}

	public QuZhongFaRandomAvaliablePaiFiller(long seed, boolean quzhongfa) {
		this.seed = seed;
		this.quzhongfa = quzhongfa;
	}

	@Override
	public void fillAvaliablePai(Ju ju) throws Exception {
		Set<MajiangPai> notPlaySet = new HashSet<>();
		notPlaySet.add(MajiangPai.chun);
		notPlaySet.add(MajiangPai.xia);
		notPlaySet.add(MajiangPai.qiu);
		notPlaySet.add(MajiangPai.dong);
		notPlaySet.add(MajiangPai.mei);
		notPlaySet.add(MajiangPai.lan);
		notPlaySet.add(MajiangPai.zhu);
		notPlaySet.add(MajiangPai.ju);
		if (quzhongfa) {
			notPlaySet.add(MajiangPai.hongzhong);
			notPlaySet.add(MajiangPai.facai);
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

		Collections.shuffle(allPaiList, new Random(seed));
		ju.getCurrentPan().setAvaliablePaiList(allPaiList);
		ju.getCurrentPan().setPaiTypeList(playPaiTypeList);
		seed++;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public boolean isQuzhongfa() {
		return quzhongfa;
	}

	public void setQuzhongfa(boolean quzhongfa) {
		this.quzhongfa = quzhongfa;
	}

}
