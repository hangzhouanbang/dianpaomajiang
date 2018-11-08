package com.anbang.qipai.dianpaomajiang.cqrs.c.domain;

import java.util.List;
import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.listener.gang.GuoGangBuGangStatisticsListener;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionUpdater;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

public class DianpaoMajiangMoActionUpdater implements MajiangPlayerMoActionUpdater {

	@Override
	public void updateActions(MajiangMoAction moAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(moAction.getActionPlayerId());
		currentPan.clearAllPlayersActionCandidates();
		List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
		Set<MajiangPai> guipaiTypeSet = player.getGuipaiTypeSet();
		MajiangPai gangmoShoupai = player.getGangmoShoupai();
		GuoGangBuGangStatisticsListener guoGangBuGangStatisticsListener = ju.getActionStatisticsListenerManager()
				.findListener(GuoGangBuGangStatisticsListener.class);
		Set<String> canNotGangPlayers = guoGangBuGangStatisticsListener.getCanNotGangPlayers();
		// 有手牌或刻子可以杠这个摸来的牌
		player.tryShoupaigangmoAndGenerateCandidateAction();
		if (!canNotGangPlayers.contains(player.getId())) {
			player.tryKezigangmoAndGenerateCandidateAction();
			// 刻子杠手牌
			player.tryKezigangshoupaiAndGenerateCandidateAction();
		}
		// 杠四个手牌
		player.tryGangsigeshoupaiAndGenerateCandidateAction();

		// 胡
		GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();

		// 天胡
		boolean couldTianhu = false;
		if (currentPan.getZhuangPlayerId().equals(player.getId())) {
			if (player.countAllFangruShoupai() == 0) {
				couldTianhu = true;
			}
		}
		DianpaoMajiangHu bestHu = DianpaoMajiangJiesuanCalculator.calculateBestZimoHu(couldTianhu, gouXingPanHu, player,
				moAction);
		if (bestHu != null) {
			bestHu.setZimo(true);
			player.addActionCandidate(new MajiangHuAction(player.getId(), bestHu));
		} else {
			// 非胡牌型特殊胡-三财神
			int guipaiCount = player.countGuipai();
			if (guipaiCount == 3) {
				DianpaoMajiangHufen hufen = new DianpaoMajiangHufen();
				hufen.setZimoHu(true);
				hufen.setWuhuxing(true);
				hufen.calculate();
				DianpaoMajiangHu sancaishenHu = new DianpaoMajiangHu(hufen);
				sancaishenHu.setZimo(true);
				player.addActionCandidate(new MajiangHuAction(player.getId(), sancaishenHu));
			}
		}
		if (guipaiTypeSet.contains(gangmoShoupai) && fangruShoupaiList.size() == 0) {
			// 当手上没有除鬼牌之外的牌时不能过
		} else {
			// 需要有“过”
			player.checkAndGenerateGuoCandidateAction();
		}
		if (player.getActionCandidates().isEmpty()) {
			// 啥也不能干，那只能打出牌
			/*
			 * 头风：抓牌后，手牌中单独一张的风牌字牌需要优先打出
			 */
			for (MajiangPai pai : fangruShoupaiList) {
				if (!guipaiTypeSet.contains(pai) && MajiangPai.isZipai(pai)) {
					if (!gangmoShoupai.equals(pai) && player.getShoupaiCalculator().count(pai) == 1) {
						player.addActionCandidate(new MajiangDaAction(player.getId(), pai));
					}
				}
			}
			if (!guipaiTypeSet.contains(gangmoShoupai) && player.getShoupaiCalculator().count(gangmoShoupai) == 0) {
				player.addActionCandidate(new MajiangDaAction(player.getId(), gangmoShoupai));
			}
		}
		// 啥也不能干，那只能打出牌
		if (player.getActionCandidates().isEmpty()) {
			player.generateDaActions();
		}

	}

}
