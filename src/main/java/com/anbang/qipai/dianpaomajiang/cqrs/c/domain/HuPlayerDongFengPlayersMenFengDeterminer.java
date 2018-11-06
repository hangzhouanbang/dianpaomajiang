package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.menfeng.PlayersMenFengDeterminer;

/**
 * 胡牌者做庄家，一炮多响时，点炮的人继续做庄家先出牌，流局庄家不变
 * 
 * @author lsc
 *
 */
public class HuPlayerDongFengPlayersMenFengDeterminer implements PlayersMenFengDeterminer {

	@Override
	public void determinePlayersMenFeng(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		PanResult latestFinishedPanResult = ju.findLatestFinishedPanResult();

	}

}
